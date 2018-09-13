/*
 * @(#)SelectColumnsFinder.java		Created at 2018/9/11
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.parser;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.NonNull;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.SimpleNode;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Find all select items within an select(or select in insert) statement.
 * <p>
 * UnSupport: update t1 set c1 = t2.c2 from t2 where t2.id = t1.id
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
//  private List<String> tables;
//  private boolean allowColumnProcessing = true;

  /**
   * There are special names, that are not table names but are parsed as tables. These names are
   * collected here and are not included in the tables - names anymore.
   */
  //private List<String> otherItemNames;

  private int deepSelect = 0;
  private boolean transformed = false;
  //one aliasTableName mapping multiple realTableName
  //one alias/readColumnName/* mapping multiple realColumnName
  //Map<aliasTableName, Map<realTableName, List<Tuple.Quadruple<alias/readColumnName/*, List<realColumnName>, deepSelect, transformed>>>>
  //lifecycle: just store in select or subSelect
  private Map<String, Map<String, List<Tuple.Quadruple<String, List<String>, Integer, Boolean>>>> columnMaps = Maps.newHashMap();

  /**
   * Override to adapt the tableName generation (e.g. with / without schema).
   *
   * @param table
   * @return
   */
  protected String extractTableName(Table table) {
    return table.getFullyQualifiedName();
  }

  /**
   * Main entry for this Tool class. A list of found tables is returned.
   *
   * @param statement
   * @return
   */
  public Map<String, List<Tuple.Quadruple<String, List<String>, Integer, Boolean>>> getSelectItemList(Statement statement) {
    init();
    statement.accept(this);
    return merge(columnMaps);
  }

  /**
   * Initializes table names collector. Important is the usage of Column instances to find table
   * names. This is only allowed for expression parsing, where a better place for tablenames could
   * not be there. For complete statements only from items are used to avoid some alias as
   * tablenames.
   * <p>
   * //@param allowColumnProcessing
   */
  protected void init() {
//    otherItemNames = new ArrayList<String>();
//    tables = new ArrayList<String>();
//    this.allowColumnProcessing = allowColumnProcessing;
    deepSelect = 0;
    transformed = false;
    columnMaps = Maps.newHashMap();
  }

  private Map<String, List<Tuple.Quadruple<String, List<String>, Integer, Boolean>>> merge(Map<String, Map<String, List<Tuple.Quadruple<String, List<String>, Integer, Boolean>>>> columnMaps) {
    return columnMaps.values().stream().collect(HashMap<String, List<Tuple.Quadruple<String, List<String>, Integer, Boolean>>>::new, (l, i) -> {
      i.keySet().forEach((item) ->
      {
        if (l.containsKey(item)) {
          l.get(item).addAll(i.get(item));
        } else {
          l.put(item, i.get(item));
        }
      });
    }, (l, r) -> {
      r.keySet().forEach((item) ->
      {
        if (l.containsKey(item)) {
          l.get(item).addAll(r.get(item));
        } else {
          l.put(item, r.get(item));
        }
      });
    });

  }

  ;

  private void processSubSelect(@NonNull SubSelect subSelect, Alias alias) {
    String aliasName = null;
    if (alias != null && alias.getName() != null) {
      aliasName = alias.getName().toUpperCase();
    }
    if (aliasName == null && subSelect.getAlias() != null && subSelect.getAlias().getName() != null) {
      aliasName = subSelect.getAlias().getName().toUpperCase();
    }
    if (aliasName == null) {
      throw new UnsupportedOperationException(MUST_DEFINE_ALIAS);
    }
    if (columnMaps.get(aliasName) != null) {
      throw new UnsupportedOperationException(THE_ALIAS_CONFLICTED);
    }
    Map<String, Map<String, List<Tuple.Quadruple<String, List<String>, Integer, Boolean>>>> handleColumnMaps = columnMaps;
    columnMaps = Maps.<String, Map<String, List<Tuple.Quadruple<String, List<String>, Integer, Boolean>>>>newHashMap();
    deepSelect++;
    processSelectBody(subSelect.getSelectBody());
    deepSelect--;
    handleColumnMaps.put(aliasName, merge(columnMaps));
    columnMaps = handleColumnMaps;
  }

  private void processSelectBody(@NonNull SelectBody selectBody) {
    selectBody.accept(this);
  }

  public void visitBinaryExpression(BinaryExpression binaryExpression) {
    binaryExpression.getLeftExpression().accept(this);
    binaryExpression.getRightExpression().accept(this);
  }

  @Override
  public void visit(Select select) {
    if (select.getWithItemsList() != null) {
      for (WithItem withItem : select.getWithItemsList()) {
        withItem.accept(this);
      }
    }
    deepSelect++;
    select.getSelectBody().accept(this);
    deepSelect--;
  }

  @Override
  public void visit(WithItem withItem) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_WITH_ITEM_YET);
//    otherItemNames.add(withItem.getName().toLowerCase());
//    withItem.getSelectBody().accept(this);
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
    String tableWholeName = extractTableName(tableName);
//    if (!otherItemNames.contains(tableWholeName.toLowerCase())
//      && !tables.contains(tableWholeName)) {
//      tables.add(tableWholeName);
//    }

    if (tableName.getAlias() == null || tableName.getAlias().getName() == null) {
      throw new UnsupportedOperationException(MUST_DEFINE_ALIAS);
    }
    if (columnMaps.get(tableName.getAlias().getName().toUpperCase()) != null) {
      throw new UnsupportedOperationException(THE_ALIAS_CONFLICTED);
    }
    Map<String, List<Tuple.Quadruple<String, List<String>, Integer, Boolean>>> realTableMap = Maps.newHashMap();
    realTableMap.put(tableWholeName.toUpperCase(), Lists.newArrayList());
    columnMaps.put(tableName.getAlias().getName().toUpperCase(), realTableMap);
  }

  @Override
  public void visit(SubSelect subSelect) {
    if (subSelect.getWithItemsList() != null) {
      for (WithItem withItem : subSelect.getWithItemsList()) {
        withItem.accept(this);
      }
    }
    processSubSelect(subSelect, subSelect.getAlias());
  }

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
    if (tableColumn.getTable() == null || tableColumn.getTable().getName() == null) {
      throw new UnsupportedOperationException(THE_COLUMN_MUST_BE_LIKE_X_Y);
    }
    Object object = ((SimpleNode) tableColumn.getASTNode().jjtGetParent().jjtGetParent()).jjtGetValue();
    if (object instanceof SelectExpressionItem) {
      SelectExpressionItem selectExpressionItem = (SelectExpressionItem) object;
      Map<String, List<Tuple.Quadruple<String, List<String>, Integer, Boolean>>> realTableMap = columnMaps.get(tableColumn.getTable().getName().toUpperCase());
      realTableMap.values().forEach((item) -> {
        String aliasName = tableColumn.getColumnName();
        if (selectExpressionItem.getAlias() != null && selectExpressionItem.getAlias().getName() != null) {
          aliasName = selectExpressionItem.getAlias().getName().toUpperCase();
        }
        final String columnName = tableColumn.getColumnName();
        List<String> realColumnList = item.stream().filter(tuple -> columnName.equalsIgnoreCase(Tuple.getFirst(tuple))).collect(ArrayList::new, (l, i) -> l.addAll(Tuple.getSecond(i)), List::addAll);
        realColumnList.add(tableColumn.getColumnName().toUpperCase());

        item.add(Tuple.of(aliasName, realColumnList, deepSelect, false));
      });
    }
//    if (allowColumnProcessing && tableColumn.getTable() != null && tableColumn.getTable().getName() != null) {
//      visit(tableColumn.getTable());
//    }
  }

  @Override
  public void visit(Division division) {
    visitBinaryExpression(division);
  }

  @Override
  public void visit(DoubleValue doubleValue) {
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
  }

  @Override
  public void visit(JdbcParameter jdbcParameter) {
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
  }

  @Override
  public void visit(TimestampValue timestampValue) {
  }

  @Override
  public void visit(TimeValue timeValue) {
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
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //just force select & insert, comments by shaneking @ 180913
//    allComparisonExpression.getSubSelect().getSelectBody().accept(this);
  }

  @Override
  public void visit(AnyComparisonExpression anyComparisonExpression) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //just force select & insert, comments by shaneking @ 180913
//    anyComparisonExpression.getSubSelect().getSelectBody().accept(this);
  }

  @Override
  public void visit(SubJoin subjoin) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_FROM_ITEM_TYPE_YET);
    //just force select & insert, comments by shaneking @ 180912
//    subjoin.getLeft().accept(this);
//    for (Join join : subjoin.getJoinList()) {
//      join.getRightItem().accept(this);
//    }
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
    throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_YET);
    //just force select & insert, comments by shaneking @ 180912
  }

  @Override
  public void visit(SetOperationList list) {
    for (SelectBody plainSelect : list.getSelects()) {
//      plainSelect.accept(this);
      processSelectBody(plainSelect);
    }
  }

  @Override
  public void visit(ExtractExpression eexpr) {
  }

  @Override
  public void visit(LateralSubSelect lateralSubSelect) {
//    lateralSubSelect.getSubSelect().getSelectBody().accept(this);
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
  }

  @Override
  public void visit(JdbcNamedParameter jdbcNamedParameter) {
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
  }

  @Override
  public void visit(JsonOperator jsonExpr) {
  }

  @Override
  public void visit(AllColumns allColumns) {
    throw new UnsupportedOperationException(THE_COLUMN_MUST_BE_LIKE_X_Y);
  }

  @Override
  public void visit(AllTableColumns allTableColumns) {
    Map<String, List<Tuple.Quadruple<String, List<String>, Integer, Boolean>>> realTableMap = columnMaps.get(allTableColumns.getTable().getName().toUpperCase());
    if (realTableMap == null) {
      throw new UnsupportedOperationException(THE_COLUMN_MUST_BE_LIKE_X_Y);
    }
    realTableMap.values().forEach((columnList) -> {
      long count = columnList.stream().filter((item) -> "*".equalsIgnoreCase(Tuple.getFirst(item))).count();
      if (count == 0) {
        columnList.add(Tuple.of("*", Lists.newArrayList(), deepSelect, transformed));
      }
    });
  }

  @Override
  public void visit(SelectExpressionItem item) {
    item.getExpression().accept(this);
  }

  @Override
  public void visit(UserVariable var) {
  }

  @Override
  public void visit(NumericBind bind) {

  }

  @Override
  public void visit(KeepExpression aexpr) {
  }

  @Override
  public void visit(MySQLGroupConcat groupConcat) {
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
      throw new UnsupportedOperationException(NOT_SUPPORTED_EXPRESSION_LIST_YET);
//      insert.getItemsList().accept(this);
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
  }

  @Override
  public void visit(DateTimeLiteralExpression literal) {

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
    throw new UnsupportedOperationException(NOT_SUPPORTED_FROM_ITEM_TYPE_YET);
    //just force select & insert, comments by shaneking @ 180912
//    parenthesis.getFromItem().accept(this);
  }
}
