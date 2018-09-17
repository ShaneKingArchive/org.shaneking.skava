/*
 * @(#)SelectColumnsFinder.java		Created at 2018/9/11
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.parser;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;
import org.shaneking.skava.ling.collect.Tuple;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Precondition: explain pass
 *
 * Find all select items within an select(or select in insert) statement.
 * <p>
 * UnSupport: update t1 set c1 = t2.c2 from t2 where t2.id = t1.id
 * ...
 */
public class SelectItemsFinder implements SelectVisitor, FromItemVisitor, ExpressionVisitor, ItemsListVisitor, SelectItemVisitor, StatementVisitor {

  private static final String NOT_SUPPORTED_STATEMENT_TYPE_YET = "Not supported statement type yet";
  private static final String NOT_SUPPORTED_EXPRESSION_YET = "Not supported expression yet";
  private static final String NOT_SUPPORTED_EXPRESSION_LIST_YET = "Not supported expression list yet";
  private static final String NOT_SUPPORTED_FROM_ITEM_TYPE_YET = "Not supported from item type yet";
  private static final String NOT_SUPPORTED_WITH_ITEM_YET = "Not supported with item yet";
  private static final String THE_COLUMN_MUST_BE_LIKE_X_Y = "The column must be like x.y";
  private static final String THE_ALIAS_CONFLICTED = "The alias conflicted";
  private static final String MUST_DEFINE_ALIAS = "Must define alias";

  private int deep = 0;

  @Getter
  @Setter
  private boolean transformed = false;
  //one aliasTableName mapping multiple realTableName
  //one alias/readColumnName/* mapping multiple realColumnName
  //lifecycle: just store in select or subSelect
  //Map<aliasTable, Tuple.Pair<List<realTable>, Map<alias/realColumn/*, List<Tuple.Quadruple<realTable, realColumn, deep, transformed>>>>>
  private Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> columns = Maps.newHashMap();

  private String selectExpressionItemAlias = null;

  /**
   * Override to adapt the tableName generation (e.g. with / without schema).
   */
  private String fullTableName(Table table) {
    return table.getFullyQualifiedName();
  }

  /**
   * Main entry for this Tool class. A list of found tables is returned.
   */
  public Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> getSelectItemList(Statement statement) {
    init();
    statement.accept(this);
    return columns;
  }

  /**
   * Initializes table names collector. Important is the usage of Column instances to find table
   * names. This is only allowed for expression parsing, where a better place for tablenames could
   * not be there. For complete statements only from items are used to avoid some alias as
   * tablenames.
   */
  private void init() {
    deep = 0;
    transformed = false;
    columns = Maps.newHashMap();
    selectExpressionItemAlias = null;
  }

  private Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>> mergeAliasTableMap(Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> columns) {
    return mergeAliasTableTuplePairCollection(columns.values());
  }

  private Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>> mergeAliasTableTuplePairCollection(@NonNull Collection<Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> collection) {
    final Set<String> rtnRealTableSet = Sets.newHashSet();
    final Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>> rtnAliasColumnMap = Maps.newHashMap();
    collection.forEach(realTableTuple -> {
      rtnRealTableSet.addAll(Tuple.getFirst(realTableTuple));
      Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>> aliasColumnMap = Tuple.getSecond(realTableTuple);
      aliasColumnMap.keySet().forEach(aliasColumn -> {
        if (rtnAliasColumnMap.get(aliasColumn) == null) {
          rtnAliasColumnMap.put(aliasColumn, aliasColumnMap.get(aliasColumn));
        } else {
          rtnAliasColumnMap.get(aliasColumn).addAll(aliasColumnMap.get(aliasColumn));
        }
      });
    });
    return Tuple.of(rtnRealTableSet, rtnAliasColumnMap);
  }

  private void processFromItem(@NonNull FromItem fromItem, Alias alias) {
    String aliasTableName = null;
    if (alias != null && alias.getName() != null) {
      aliasTableName = alias.getName().toUpperCase();
    }
    if (aliasTableName == null && fromItem.getAlias() != null && fromItem.getAlias().getName() != null) {
      aliasTableName = fromItem.getAlias().getName().toUpperCase();
    }
    if (aliasTableName == null) {
      throw new UnsupportedOperationException(MUST_DEFINE_ALIAS);
    } else {
      Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> handleColumns = columns;
      columns = Maps.<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>>newHashMap();
      deep++;
      fromItem.accept(this);
      deep--;
      if (handleColumns.get(aliasTableName) == null) {
        handleColumns.put(aliasTableName, mergeAliasTableMap(columns));
      } else {
        handleColumns.put(aliasTableName, mergeAliasTableTuplePairCollection(Lists.newArrayList(mergeAliasTableMap(columns), handleColumns.get(aliasTableName))));
      }
      columns = handleColumns;
    }
  }

  private void processSelectBody(@NonNull SelectBody selectBody) {
    deep++;
    selectBody.accept(this);
    deep--;
  }

  private void processSubSelect(@NonNull SubSelect subSelect, Alias alias) {
    String aliasTableName = null;
    if (alias != null && alias.getName() != null) {
      aliasTableName = alias.getName().toUpperCase();
    }
    if (aliasTableName == null && subSelect.getAlias() != null && subSelect.getAlias().getName() != null) {
      aliasTableName = subSelect.getAlias().getName().toUpperCase();
    }
    if (aliasTableName == null) {
      processSelectBody(subSelect.getSelectBody());
    } else {
      Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> handleColumns = columns;
      columns = Maps.<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>>newHashMap();
      processSelectBody(subSelect.getSelectBody());
      if (handleColumns.get(aliasTableName) == null) {
        handleColumns.put(aliasTableName, mergeAliasTableMap(columns));
      } else {
        handleColumns.put(aliasTableName, mergeAliasTableTuplePairCollection(Lists.newArrayList(mergeAliasTableMap(columns), handleColumns.get(aliasTableName))));
      }
      columns = handleColumns;
    }
  }

  private void processWithItem(@NonNull WithItem withItem) {
    deep++;
    withItem.accept(this);
    deep--;
  }

  private Set<Tuple.Quadruple<String, String, Integer, Boolean>> updateDeepAndTransformed(@NonNull Set<Tuple.Quadruple<String, String, Integer, Boolean>> realColumnSet) {
    return realColumnSet.stream().map((tuple) -> Tuple.of(Tuple.getFirst(tuple), Tuple.getSecond(tuple), Math.min(deep, Tuple.getThird(tuple)), transformed || Tuple.getFourth(tuple))).collect(Collectors.toSet());
  }

  private void visitBinaryExpression(BinaryExpression binaryExpression) {
    binaryExpression.getLeftExpression().accept(this);
    binaryExpression.getRightExpression().accept(this);
  }

  @Override
  public void visit(Select select) {
    if (select.getWithItemsList() != null) {
      for (WithItem withItem : select.getWithItemsList()) {
        processWithItem(withItem);
      }
    }
    processSelectBody(select.getSelectBody());
  }

  @Override
  public void visit(WithItem withItem) {
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> handleColumns = columns;
    columns = Maps.<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>>newHashMap();
    processSelectBody(withItem.getSelectBody());
    String aliasTableName = withItem.getName().toUpperCase();
    if (handleColumns.get(aliasTableName) == null) {
      handleColumns.put(aliasTableName, mergeAliasTableMap(columns));
    } else {
      handleColumns.put(aliasTableName, mergeAliasTableTuplePairCollection(Lists.newArrayList(mergeAliasTableMap(columns), handleColumns.get(aliasTableName))));
    }
    columns = handleColumns;
  }

  @Override
  public void visit(PlainSelect plainSelect) {
    if (plainSelect.getFromItem() != null) {
      plainSelect.getFromItem().accept(this);
    }

    if (plainSelect.getJoins() != null) {
      for (Join join : plainSelect.getJoins()) {
        join.getRightItem().accept(this);
      }
    }

    if (plainSelect.getSelectItems() != null) {
      for (SelectItem item : plainSelect.getSelectItems()) {
        item.accept(this);
      }
    }
    //just force select & insert, comments by shaneking @ 180912
//    if (plainSelect.getWhere() != null) {
//      plainSelect.getWhere().accept(this);
//    }
//
//    if (plainSelect.getHaving() != null) {
//      plainSelect.getHaving().accept(this);
//    }
//
//    if (plainSelect.getOracleHierarchical() != null) {
//      plainSelect.getOracleHierarchical().accept(this);
//    }
  }

  @Override
  public void visit(Table tableName) {
    String realTableName = fullTableName(tableName).toUpperCase();

    String aliasTableName = realTableName;
    if (tableName.getAlias() != null && tableName.getAlias().getName() != null) {
      aliasTableName = tableName.getAlias().getName().toUpperCase();
    }

    Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>> realTableTuplePair = columns.get(realTableName);
    if (realTableTuplePair == null) {
      if (columns.get(aliasTableName) != null) {
        throw new UnsupportedOperationException(THE_ALIAS_CONFLICTED);
      }
      columns.put(aliasTableName, Tuple.of(Sets.newHashSet(realTableName), Maps.newHashMap()));
    } else {
      //@see test.skava.sql.parser.SelectItemsFinderTest.testGetSelectItemListWith()
      //WITH TESTWITH as (SELECT ALIAS_TABLE1.* FROM MY_TABLE1 as ALIAS_TABLE1) SELECT TESTWITH.* FROM TESTWITH
      //above ignore


      //@see test.skava.sql.parser.SelectItemsFinderTest.testGetSelectItemListWithStmt()
      //WITH TESTWITH as (SELECT ALIAS_TABLE1.* FROM MY_TABLE1 as ALIAS_TABLE1) SELECT T.* FROM TESTWITH AS T
      if (!aliasTableName.equalsIgnoreCase(realTableName)) {
        columns.put(aliasTableName, mergeAliasTableTuplePairCollection(Lists.<Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>>newArrayList(realTableTuplePair)));
      }
    }
  }

  @Override
  public void visit(SubSelect subSelect) {
    if (subSelect.getWithItemsList() != null) {
      for (WithItem withItem : subSelect.getWithItemsList()) {
        processWithItem(withItem);
      }
    }
    processSubSelect(subSelect, subSelect.getAlias());
  }

  @SelectItemsFinderTransformed
  @Override
  public void visit(Addition addition) {
    visitBinaryExpression(addition);
  }

  @Override
  public void visit(AndExpression andExpression) {
    visitBinaryExpression(andExpression);
  }

  @Override
  public void visit(Between between) {
    between.getLeftExpression().accept(this);
    between.getBetweenExpressionStart().accept(this);
    between.getBetweenExpressionEnd().accept(this);
  }

  @Override
  public void visit(Column tableColumn) {
    if (tableColumn.getTable() == null) {
      throw new UnsupportedOperationException(THE_COLUMN_MUST_BE_LIKE_X_Y);
    }
    String aliasTableName = fullTableName(tableColumn.getTable());
    if (Strings.isNullOrEmpty(aliasTableName)) {
      throw new UnsupportedOperationException(THE_COLUMN_MUST_BE_LIKE_X_Y);
    }
    aliasTableName = aliasTableName.toUpperCase();

    Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>> aliasTableTuple = columns.get(aliasTableName);
    if (aliasTableTuple == null) {
      //schema.table.column or schema.table.*
      aliasTableTuple = Tuple.of(Sets.newHashSet(aliasTableName), Maps.newHashMap());
      columns.put(aliasTableName, aliasTableTuple);
    }

    Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>> aliasColumnMap = Tuple.getSecond(aliasTableTuple);
    String columnName = tableColumn.getColumnName().toUpperCase();
    Set<Tuple.Quadruple<String, String, Integer, Boolean>> columnSet = aliasColumnMap.get(columnName);
    if (columnSet == null) {
      //columnName is realColumnName
      aliasColumnMap.put(columnName, Tuple.getFirst(aliasTableTuple).stream().map(realTable -> Tuple.of(realTable, tableColumn.getColumnName().toUpperCase(), deep, transformed)).collect(Collectors.toSet()));
    } else {
      //columnName is aliasColumnName
      aliasColumnMap.put(columnName, updateDeepAndTransformed(columnSet));
    }

    if (!Strings.isNullOrEmpty(selectExpressionItemAlias)) {
      Set<Tuple.Quadruple<String, String, Integer, Boolean>> aliasColumnSet = aliasColumnMap.get(columnName);
      if (aliasColumnSet == null) {
        //columnName is realColumnName
        aliasColumnMap.put(selectExpressionItemAlias, Tuple.getFirst(aliasTableTuple).stream().map(realTable -> Tuple.of(realTable, tableColumn.getColumnName().toUpperCase(), deep, transformed)).collect(Collectors.toSet()));
      } else {
        //columnName is aliasColumnName
        aliasColumnMap.put(selectExpressionItemAlias, updateDeepAndTransformed(aliasColumnSet));
      }
    }
//    if (allowColumnProcessing && tableColumn.getTable() != null && tableColumn.getTable().getName() != null) {
//      visit(tableColumn.getTable());
//    }
  }

  @SelectItemsFinderTransformed
  @Override
  public void visit(Division division) {
    visitBinaryExpression(division);
  }

  @Override
  public void visit(DoubleValue doubleValue) {
    //no column, ignore
  }

  @Override
  public void visit(EqualsTo equalsTo) {
    visitBinaryExpression(equalsTo);
  }

  @Override
  public void visit(Function function) {
    ExpressionList exprList = function.getParameters();
    if (exprList != null) {
      visit(exprList);
    }
  }

  @Override
  public void visit(GreaterThan greaterThan) {
    visitBinaryExpression(greaterThan);
  }

  @Override
  public void visit(GreaterThanEquals greaterThanEquals) {
    visitBinaryExpression(greaterThanEquals);
  }

  @Override
  public void visit(InExpression inExpression) {
    if (inExpression.getLeftExpression() != null) {
      inExpression.getLeftExpression().accept(this);
    } else if (inExpression.getLeftItemsList() != null) {
      inExpression.getLeftItemsList().accept(this);
    }
    inExpression.getRightItemsList().accept(this);
  }

  @Override
  public void visit(SignedExpression signedExpression) {
    signedExpression.getExpression().accept(this);
  }

  @Override
  public void visit(IsNullExpression isNullExpression) {
    isNullExpression.getLeftExpression().accept(this);
  }

  @Override
  public void visit(JdbcParameter jdbcParameter) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //unknown expression, fix when meeting
  }

  @Override
  public void visit(LikeExpression likeExpression) {
    visitBinaryExpression(likeExpression);
  }

  @Override
  public void visit(ExistsExpression existsExpression) {
    existsExpression.getRightExpression().accept(this);
  }

  @Override
  public void visit(LongValue longValue) {
    //no column, ignore
  }

  @Override
  public void visit(MinorThan minorThan) {
    visitBinaryExpression(minorThan);
  }

  @Override
  public void visit(MinorThanEquals minorThanEquals) {
    visitBinaryExpression(minorThanEquals);
  }

  @Override
  public void visit(Multiplication multiplication) {
    visitBinaryExpression(multiplication);
  }

  @Override
  public void visit(NotEqualsTo notEqualsTo) {
    visitBinaryExpression(notEqualsTo);
  }

  @Override
  public void visit(NullValue nullValue) {
    //no column, ignore
  }

  @Override
  public void visit(OrExpression orExpression) {
    visitBinaryExpression(orExpression);
  }

  @Override
  public void visit(Parenthesis parenthesis) {
    parenthesis.getExpression().accept(this);
  }

  @Override
  public void visit(StringValue stringValue) {
    //no column, ignore
  }

  @Override
  public void visit(Subtraction subtraction) {
    visitBinaryExpression(subtraction);
  }

  @Override
  public void visit(NotExpression notExpr) {
    notExpr.getExpression().accept(this);
  }

  @Override
  public void visit(BitwiseRightShift expr) {
    visitBinaryExpression(expr);
  }

  @Override
  public void visit(BitwiseLeftShift expr) {
    visitBinaryExpression(expr);
  }

  @Override
  public void visit(ExpressionList expressionList) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_LIST_YET);
    //just force select & insert, comments by shaneking @ 180912
//    for (Expression expression : expressionList.getExpressions()) {
//      expression.accept(this);
//    }
  }

  @Override
  public void visit(DateValue dateValue) {
    //no column, ignore
  }

  @Override
  public void visit(TimestampValue timestampValue) {
    //no column, ignore
  }

  @Override
  public void visit(TimeValue timeValue) {
    //no column, ignore
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.jsqlparser.expression.ExpressionVisitor#visit(net.sf.jsqlparser.expression.CaseExpression)
   */
  @Override
  public void visit(CaseExpression caseExpression) {
    if (caseExpression.getSwitchExpression() != null) {
      caseExpression.getSwitchExpression().accept(this);
    }
    if (caseExpression.getWhenClauses() != null) {
      for (WhenClause when : caseExpression.getWhenClauses()) {
        when.accept(this);
      }
    }
    if (caseExpression.getElseExpression() != null) {
      caseExpression.getElseExpression().accept(this);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.jsqlparser.expression.ExpressionVisitor#visit(net.sf.jsqlparser.expression.WhenClause)
   */
  @Override
  public void visit(WhenClause whenClause) {
    if (whenClause.getWhenExpression() != null) {
      whenClause.getWhenExpression().accept(this);
    }
    if (whenClause.getThenExpression() != null) {
      whenClause.getThenExpression().accept(this);
    }
  }

  @Override
  public void visit(AllComparisonExpression allComparisonExpression) {
    processSubSelect(allComparisonExpression.getSubSelect(), null);
  }

  @Override
  public void visit(AnyComparisonExpression anyComparisonExpression) {
    processSubSelect(anyComparisonExpression.getSubSelect(), null);
  }

  @Override
  public void visit(SubJoin subjoin) {
    processFromItem(subjoin.getLeft(), subjoin.getAlias());
    for (Join join : subjoin.getJoinList()) {
      processFromItem(join.getRightItem(), null);
    }
  }

  @Override
  public void visit(Concat concat) {
    visitBinaryExpression(concat);
  }

  @Override
  public void visit(Matches matches) {
    visitBinaryExpression(matches);
  }

  @Override
  public void visit(BitwiseAnd bitwiseAnd) {
    visitBinaryExpression(bitwiseAnd);
  }

  @Override
  public void visit(BitwiseOr bitwiseOr) {
    visitBinaryExpression(bitwiseOr);
  }

  @Override
  public void visit(BitwiseXor bitwiseXor) {
    visitBinaryExpression(bitwiseXor);
  }

  @Override
  public void visit(CastExpression cast) {
    cast.getLeftExpression().accept(this);
  }

  @Override
  public void visit(Modulo modulo) {
    visitBinaryExpression(modulo);
  }

  @Override
  public void visit(AnalyticExpression analytic) {
    analytic.getExpression().accept(this);
    analytic.getPartitionExpressionList().accept(this);
    analytic.getOffset().accept(this);
    analytic.getDefaultValue().accept(this);
    analytic.getKeep().accept(this);
//    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //just force select & insert, comments by shaneking @ 180912
  }

  @Override
  public void visit(SetOperationList list) {
    for (SelectBody plainSelect : list.getSelects()) {
      plainSelect.accept(this);
    }
  }

  @Override
  public void visit(ExtractExpression eexpr) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //unknown expression, fix when meeting
  }

  @Override
  public void visit(LateralSubSelect lateralSubSelect) {
    processSubSelect(lateralSubSelect.getSubSelect(), lateralSubSelect.getAlias());
  }

  @Override
  public void visit(MultiExpressionList multiExprList) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_LIST_YET);
    //just force select & insert, comments by shaneking @ 180912
//    for (ExpressionList exprList : multiExprList.getExprList()) {
//      exprList.accept(this);
//    }
  }

  @Override
  public void visit(ValuesList valuesList) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_FROM_ITEM_TYPE_YET);
    //just force select & insert, comments by shaneking @ 180912
  }

  @Override
  public void visit(IntervalExpression iexpr) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //unknown expression, fix when meeting
  }

  @Override
  public void visit(JdbcNamedParameter jdbcNamedParameter) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //unknown expression, fix when meeting
  }

  @Override
  public void visit(OracleHierarchicalExpression oexpr) {
    if (oexpr.getStartExpression() != null) {
      oexpr.getStartExpression().accept(this);
    }

    if (oexpr.getConnectExpression() != null) {
      oexpr.getConnectExpression().accept(this);
    }
  }

  @Override
  public void visit(RegExpMatchOperator rexpr) {
    visitBinaryExpression(rexpr);
  }

  @Override
  public void visit(RegExpMySQLOperator rexpr) {
    visitBinaryExpression(rexpr);
  }

  @Override
  public void visit(JsonExpression jsonExpr) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //unknown expression, fix when meeting
  }

  @Override
  public void visit(JsonOperator jsonExpr) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //unknown expression, fix when meeting
  }

  @Override
  public void visit(AllColumns allColumns) {
    throw new UnsupportedOperationException(THE_COLUMN_MUST_BE_LIKE_X_Y);
  }

  @Override
  public void visit(AllTableColumns allTableColumns) {
    if (allTableColumns.getTable() == null) {
      throw new UnsupportedOperationException(THE_COLUMN_MUST_BE_LIKE_X_Y);
    }
    String aliasTableName = fullTableName(allTableColumns.getTable());
    if (Strings.isNullOrEmpty(aliasTableName)) {
      throw new UnsupportedOperationException(THE_COLUMN_MUST_BE_LIKE_X_Y);
    }
    aliasTableName = aliasTableName.toUpperCase();

    Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>> aliasTableTuple = columns.get(aliasTableName);
    if (aliasTableTuple == null) {
      //schema.table.*
      aliasTableTuple = Tuple.of(Sets.newHashSet(aliasTableName), Maps.newHashMap());
      columns.put(aliasTableName, aliasTableTuple);
    }

    Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>> aliasColumnMap = Tuple.getSecond(aliasTableTuple);
    Set<Tuple.Quadruple<String, String, Integer, Boolean>> aliasColumnSet = aliasColumnMap.get("*");
    if (aliasColumnSet == null) {
      //columnName is realColumnName
      Set<Tuple.Quadruple<String, String, Integer, Boolean>> allAliasColumnSet = aliasColumnMap.values().stream().collect(HashSet::new, Set::addAll, Set::addAll);
      if (allAliasColumnSet.size() == 0) {
        aliasColumnMap.put("*", Tuple.getFirst(aliasTableTuple).stream().map(realTable -> Tuple.of(realTable, "*", deep, transformed)).collect(Collectors.toSet()));
      } else {
        aliasColumnMap.put("*", updateDeepAndTransformed(allAliasColumnSet));
      }
    } else {
      //columnName is aliasColumnName
      aliasColumnMap.put("*", updateDeepAndTransformed(aliasColumnSet));
    }
  }

  @Override
  public void visit(SelectExpressionItem item) {
    String handleSelectExpressionItemAlias = selectExpressionItemAlias;
    selectExpressionItemAlias = (item.getAlias() == null ? null : (item.getAlias().getName() == null ? null : item.getAlias().getName().toUpperCase()));
    item.getExpression().accept(this);
    selectExpressionItemAlias = handleSelectExpressionItemAlias;
  }

  @Override
  public void visit(UserVariable var) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //unknown expression, fix when meeting
  }

  @Override
  public void visit(NumericBind bind) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //unknown expression, fix when meeting
  }

  @Override
  public void visit(KeepExpression aexpr) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //unknown expression, fix when meeting
  }

  @Override
  public void visit(MySQLGroupConcat groupConcat) {
    groupConcat.getExpressionList().accept(this);
  }

  @Override
  public void visit(ValueListExpression valueList) {
    valueList.getExpressionList().accept(this);
  }

  @Override
  public void visit(Delete delete) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
    //just force select & insert, comments by shaneking @ 180912
//    visit(delete.getTable());
//
//    if (delete.getJoins() != null) {
//      for (Join join : delete.getJoins()) {
//        join.getRightItem().accept(this);
//      }
//    }
//
//    if (delete.getWhere() != null) {
//      delete.getWhere().accept(this);
//    }
  }

  @Override
  public void visit(Update update) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
    //just force select & insert, comments by shaneking @ 180912
//    for (Table table : update.getTables()) {
//      visit(table);
//    }
//    if (update.getExpressions() != null) {
//      for (Expression expression : update.getExpressions()) {
//        expression.accept(this);
//      }
//    }
//
//    if (update.getFromItem() != null) {
//      update.getFromItem().accept(this);
//    }
//
//    if (update.getJoins() != null) {
//      for (Join join : update.getJoins()) {
//        join.getRightItem().accept(this);
//      }
//    }
//
//    if (update.getWhere() != null) {
//      update.getWhere().accept(this);
//    }
  }

  @Override
  public void visit(Insert insert) {
    //just force select & insert, comments by shaneking @ 180912
//    visit(insert.getTable());
    if (insert.getItemsList() != null) {
      //TODO test.skava.sql.parser.SelectItemsFinderTest.testGetSelectItemListFromInsert()
      insert.getItemsList().accept(this);
    }
    if (insert.getSelect() != null) {
      visit(insert.getSelect());
    }
  }

  @Override
  public void visit(Replace replace) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
    //just force select & insert, comments by shaneking @ 180912
//    visit(replace.getTable());
//    if (replace.getExpressions() != null) {
//      for (Expression expression : replace.getExpressions()) {
//        expression.accept(this);
//      }
//    }
//    if (replace.getItemsList() != null) {
//      replace.getItemsList().accept(this);
//    }
  }

  @Override
  public void visit(Drop drop) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
  }

  @Override
  public void visit(Truncate truncate) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
  }

  @Override
  public void visit(CreateIndex createIndex) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
  }

  @Override
  public void visit(CreateTable create) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
    //just force select & insert, comments by shaneking @ 180912
//    visit(create.getTable());
//    if (create.getSelect() != null) {
//      create.getSelect().accept(this);
//    }
  }

  @Override
  public void visit(CreateView createView) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
  }

  @Override
  public void visit(Alter alter) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
  }

  @Override
  public void visit(Statements stmts) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
  }

  @Override
  public void visit(Execute execute) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
  }

  @Override
  public void visit(SetStatement set) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
  }

  @Override
  public void visit(RowConstructor rowConstructor) {
    for (Expression expr : rowConstructor.getExprList().getExpressions()) {
      expr.accept(this);
    }
  }

  @Override
  public void visit(HexValue hexValue) {
    //no column, ignore
  }

  @Override
  public void visit(Merge merge) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
    //just force select & insert, comments by shaneking @ 180912
//    visit(merge.getTable());
//    if (merge.getUsingTable() != null) {
//      merge.getUsingTable().accept(this);
//    } else if (merge.getUsingSelect() != null) {
//      merge.getUsingSelect().accept((FromItemVisitor) this);
//    }
  }

  @Override
  public void visit(OracleHint hint) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //unknown expression, fix when meeting
  }

  @Override
  public void visit(TableFunction valuesList) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_FROM_ITEM_TYPE_YET);
    //just force select & insert, comments by shaneking @ 180912
  }

  @Override
  public void visit(AlterView alterView) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
  }

  @Override
  public void visit(TimeKeyExpression timeKeyExpression) {
    //no column, ignore
  }

  @Override
  public void visit(DateTimeLiteralExpression literal) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //unknown expression, fix when meeting
  }

  @Override
  public void visit(Commit commit) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
    //just force select & insert, comments by shaneking @ 180912
  }

  @Override
  public void visit(Upsert upsert) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
    //just force select & insert, comments by shaneking @ 180912
//    visit(upsert.getTable());
//    if (upsert.getItemsList() != null) {
//      upsert.getItemsList().accept(this);
//    }
//    if (upsert.getSelect() != null) {
//      visit(upsert.getSelect());
//    }
  }

  @Override
  public void visit(UseStatement use) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
    //just force select & insert, comments by shaneking @ 180912
  }

  @Override
  public void visit(ParenthesisFromItem parenthesis) {
    processFromItem(parenthesis.getFromItem(), parenthesis.getAlias());
  }
}
