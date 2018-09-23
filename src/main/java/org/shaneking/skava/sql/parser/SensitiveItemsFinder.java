/*
 * @(#)SelectColumnsFinder.java		Created at 2018/9/11
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.parser;

import com.google.common.base.Joiner;
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
import org.shaneking.skava.ling.lang.String0;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Precondition: explain pass
 * Order: inter -> outer
 * Focus:
 * 1.select item (x.y)
 * 2.column transformed
 * <p>
 * https://www.postgresql.org/docs/current/static/sql-select.html
 * [ WITH [ RECURSIVE ] with_query [, ...] ]
 * SELECT [ ALL | DISTINCT [ ON ( expression [, ...] ) ] ]
 * [ * | expression [ [ AS ] output_name ] [, ...] ]
 * [ FROM from_item [, ...] ]
 * [ WHERE condition ]
 * [ GROUP BY grouping_element [, ...] ]
 * [ HAVING condition [, ...] ]
 * [ WINDOW window_name AS ( window_definition ) [, ...] ]
 * [ { UNION | INTERSECT | EXCEPT } [ ALL | DISTINCT ] select ]
 * [ ORDER BY expression [ ASC | DESC | USING operator ] [ NULLS { FIRST | LAST } ] [, ...] ]
 * [ LIMIT { count | ALL } ]
 * [ OFFSET start [ ROW | ROWS ] ]
 * [ FETCH { FIRST | NEXT } [ count ] { ROW | ROWS } ONLY ]
 * [ FOR { UPDATE | NO KEY UPDATE | SHARE | KEY SHARE } [ OF table_name [, ...] ] [ NOWAIT | SKIP LOCKED ] [...] ]
 * <p>
 * where from_item can be one of:
 * <p>
 * [ ONLY ] table_name [ * ] [ [ AS ] alias [ ( column_alias [, ...] ) ] ]
 * [ TABLESAMPLE sampling_method ( argument [, ...] ) [ REPEATABLE ( seed ) ] ]
 * [ LATERAL ] ( select ) [ AS ] alias [ ( column_alias [, ...] ) ]
 * with_query_name [ [ AS ] alias [ ( column_alias [, ...] ) ] ]
 * [ LATERAL ] function_name ( [ argument [, ...] ] )
 * [ WITH ORDINALITY ] [ [ AS ] alias [ ( column_alias [, ...] ) ] ]
 * [ LATERAL ] function_name ( [ argument [, ...] ] ) [ AS ] alias ( column_definition [, ...] )
 * [ LATERAL ] function_name ( [ argument [, ...] ] ) AS ( column_definition [, ...] )
 * [ LATERAL ] ROWS FROM( function_name ( [ argument [, ...] ] ) [ AS ( column_definition [, ...] ) ] [, ...] )
 * [ WITH ORDINALITY ] [ [ AS ] alias [ ( column_alias [, ...] ) ] ]
 * from_item [ NATURAL ] join_type from_item [ ON join_condition | USING ( join_column [, ...] ) ]
 * <p>
 * and grouping_element can be one of:
 * <p>
 * ( )
 * expression
 * ( expression [, ...] )
 * ROLLUP ( { expression | ( expression [, ...] ) } [, ...] )
 * CUBE ( { expression | ( expression [, ...] ) } [, ...] )
 * GROUPING SETS ( grouping_element [, ...] )
 * <p>
 * and with_query is:
 * <p>
 * with_query_name [ ( column_name [, ...] ) ] AS ( select | values | insert | update | delete )
 * <p>
 * TABLE [ ONLY ] table_name [ * ]
 */
public class SensitiveItemsFinder implements SelectVisitor, FromItemVisitor, ExpressionVisitor, ItemsListVisitor, SelectItemVisitor, StatementVisitor {

  public static final String PATH_OF_SELECT = "Select";
  public static final String PATH_OF_SELECT_EXPRESSION_ITEM = "SelectExpressionItem";
  public static final String PATH_OF_SUB_SELECT = "SubSelect";
  public static final String PATH_OF_WITH_ITEM = "WithItem";
  private static final String NOT_SUPPORTED_STATEMENT_TYPE_YET = "Not supported statement type yet";
  private static final String NOT_SUPPORTED_EXPRESSION_YET = "Not supported expression yet";
  private static final String NOT_SUPPORTED_EXPRESSION_LIST_YET = "Not supported expression list yet";
  private static final String NOT_SUPPORTED_SELECT_ITEM_LIST_IN_WITH_ITEM_YET = "Not supported select item list in with item yet";
  private static final String NOT_SUPPORTED_FROM_ITEM_TYPE_YET = "Not supported from item type yet";
  private static final String NOT_SUPPORTED_WITH_ITEM_YET = "Not supported with item yet";
  private static final String THE_COLUMN_MUST_BE_LIKE_X_Y = "The column must be like x.y";
  private static final String THE_ALIAS_CONFLICTED = "The alias conflicted";
  private static final String MUST_DEFINE_ALIAS = "Must define alias";
  /**
   * one aliasTableName mapping multiple realTableName
   * one alias/readColumnName/* mapping multiple realColumnName
   * lifecycle: just store in select or subSelect
   * Map<aliasTable, Tuple.Pair<List<realTable>, Map<alias/realColumn/*, List<Tuple.Quadruple<realTable, realColumn, path, transformed>>>>>
   */
  @Getter
  @Setter
  private Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = Maps.newHashMap();
  /**
   * SensitiveItemsFinderPath
   * <p>
   * (Sub)Select/WithItem/SelectExpressionItem
   */
  @Getter
  private Stack<String> pathStack = new Stack<>();
  /**
   * can't handle itemMap in select expression item
   * <p>
   * null:out select expression item
   * String0.EMPTY:in select expression item without alias
   * alias:in select expression item with alias
   */
  private String selectExpressionItemAlias = null;

  /**
   * In (Sub)Select of selectExpressionItem: table can see withStack, column can see selectExpressionItemStack.
   * <p>
   * SELECT (select (select a.host+b.host+c.host as c3 from mysql.user c where c.user = a.user) as c2 from mysql.user b where b.user = a.user) as c1 FROM mysql.user a;
   */
  private Stack<Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>>> selectExpressionItemStack = new Stack<>();
  /**
   * SensitiveItemsFinderTransformed
   * <p>
   * Addition...
   */
  @Getter
  @Setter
  private boolean transformed = false;

  /**
   * Control access range, all (Sub)Select can see WithItemsList content, withStack exchanged by self in annotation
   * <p>
   * push when (Sub)Select has WithItemsList
   * pop when exit (Sub)Select  //SensitiveItemsFinderPath
   * <p>
   * scenario1:with a as (...), b as (...a...) select * from (with c as (...), d as (...c...))
   * solution1:because c can use a, but a can't use c, so need stack pull pop
   */
  private Stack<Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>>> withStack = new Stack<>();

  /**
   * Main entry for this Tool class. A list of found tables is returned.
   */
  public Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> findItemMap(Statement statement) {
    init();
    statement.accept(this);
    return itemMap;
  }

  private Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>> firstTuple(Stack<Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>>> stack, String key) {
    Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>> rtn = null;
    if (stack.size() > 0) {
      for (int i = stack.size() - 1; i >= 0; i--) {
        rtn = stack.get(i).get(key);
        if (rtn != null) {
          break;
        }
      }
    }
    return rtn;
  }

  private Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>> firstTuple(String key) {
    Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>> rtn = firstTuple(selectExpressionItemStack, key);
    if (rtn == null) {
      rtn = itemMap.get(key);
    }
    if (rtn == null) {
      rtn = firstTuple(withStack, key);
    }
    return rtn;
  }

  /**
   * Initializes table names collector. Important is the usage of Column instances to find table
   * names. This is only allowed for expression parsing, where a better place for tablenames could
   * not be there. For complete statements only from items are used to avoid some alias as
   * tablenames.
   */
  private void init() {
    itemMap = Maps.newHashMap();
    pathStack = new Stack<>();
    selectExpressionItemAlias = null;
    selectExpressionItemStack = new Stack<>();
    transformed = false;
    withStack = new Stack<>();
  }

  Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>> mergeAliasTableMap(Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> columns) {
    return mergeAliasTableTuplePairCollection(columns.values());
  }

  Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>> mergeAliasTableTuplePairCollection(@NonNull Collection<Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> collection) {
    final Set<String> rtnRealTableSet = Sets.newHashSet();
    final Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>> rtnAliasColumnMap = Maps.newHashMap();
    collection.forEach(realTableTuple -> {
      rtnRealTableSet.addAll(Tuple.getFirst(realTableTuple));
      Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>> aliasColumnMap = Tuple.getSecond(realTableTuple);
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

//  private void processFromItem(@NonNull FromItem fromItem, Alias alias) {
//    String aliasTableName = null;
//    if (alias != null && alias.getName() != null) {
//      aliasTableName = alias.getName().toUpperCase();
//    }
//    if (aliasTableName == null && fromItem.getAlias() != null && fromItem.getAlias().getName() != null) {
//      aliasTableName = fromItem.getAlias().getName().toUpperCase();
//    }
//    if (aliasTableName == null) {
//      throw new UnsupportedOperationException(Joiner.on(String0.COLON).join(Lists.newArrayList(MUST_DEFINE_ALIAS, fromItem)));
//    } else {
//      Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> handleItemMap = itemMap;
//      itemMap = Maps.<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>>newHashMap();
//      fromItem.accept(this);
//      if (handleItemMap.get(aliasTableName) == null) {
//        handleItemMap.put(aliasTableName, mergeAliasTableMap(itemMap));
//      } else {
//        handleItemMap.put(aliasTableName, mergeAliasTableTuplePairCollection(Lists.newArrayList(mergeAliasTableMap(itemMap), handleItemMap.get(aliasTableName))));
//      }
//      itemMap = handleItemMap;
//    }
//  }
//
//  /**
//   * add subSelect layer for alias
//   */
//  private void processSubSelect(@NonNull SubSelect subSelect, Alias alias) {
//    String aliasTableName = null;
//    if (alias != null && alias.getName() != null) {
//      aliasTableName = alias.getName().toUpperCase();
//    }
//    if (aliasTableName == null && subSelect.getAlias() != null && subSelect.getAlias().getName() != null) {
//      aliasTableName = subSelect.getAlias().getName().toUpperCase();
//    }
//    if (aliasTableName == null) {
//      subSelect.getSelectBody().accept(this);
//    } else {
//      Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> handleItemMap = itemMap;
//      itemMap = Maps.<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>>newHashMap();
//      subSelect.getSelectBody().accept(this);
//      if (handleItemMap.get(aliasTableName) == null) {
//        handleItemMap.put(aliasTableName, mergeAliasTableMap(itemMap));
//      } else {
//        handleItemMap.put(aliasTableName, mergeAliasTableTuplePairCollection(Lists.newArrayList(mergeAliasTableMap(itemMap), handleItemMap.get(aliasTableName))));
//      }
//      itemMap = handleItemMap;
//    }
//  }

  /**
   * add withItemsList layer
   */
  private void processWithItemsList(@NonNull List<WithItem> withItemsList) {
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> handleItemMap = itemMap;
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> withItemMap = Maps.newHashMap();
    for (WithItem withItem : withItemsList) {
      itemMap = Maps.newHashMap();
      withItem.accept(this);
      withItemMap.put(withItem.getName().toUpperCase(), mergeAliasTableMap(itemMap));
    }
    //key point of the method
    withStack.push(withItemMap);
    itemMap = handleItemMap;
  }

  private String updatePath(String path) {
    return path.endsWith(PATH_OF_SELECT_EXPRESSION_ITEM) ? path : (path.split(String0.ARROW).length < pathStack.size() ? path : Joiner.on(String0.ARROW).join(pathStack));
  }

  private Set<Tuple.Quadruple<String, String, String, Boolean>> updatePathAndTransformed(@NonNull Set<Tuple.Quadruple<String, String, String, Boolean>> realColumnSet) {
    return realColumnSet.stream().map((tuple) -> Tuple.of(Tuple.getFirst(tuple), Tuple.getSecond(tuple), updatePath(Tuple.getThird(tuple)), transformed || Tuple.getFourth(tuple))).collect(Collectors.toSet());
  }

  private void visitBinaryExpression(BinaryExpression binaryExpression) {
    binaryExpression.getLeftExpression().accept(this);
    binaryExpression.getRightExpression().accept(this);
  }

  @SensitiveItemsFinderPath
  @Override
  public void visit(Select select) {
    if (select.getWithItemsList() != null && select.getWithItemsList().size() > 0) {
      processWithItemsList(select.getWithItemsList());
    }
    select.getSelectBody().accept(this);
  }

  @SensitiveItemsFinderPath
  @Override
  public void visit(WithItem withItem) {
    if (withItem.getWithItemList() != null) {
      throw new UnsupportedOperationException(Joiner.on(String0.COLON).join(Lists.newArrayList(NOT_SUPPORTED_SELECT_ITEM_LIST_IN_WITH_ITEM_YET, withItem)));
    }
    withItem.getSelectBody().accept(this);
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

  /**
   * can use itemStack, or new
   */
  @Override
  public void visit(Table tableName) {
    String realTableName = tableName.getFullyQualifiedName().toUpperCase();

    String aliasTableName = realTableName;
    if (tableName.getAlias() != null && tableName.getAlias().getName() != null) {
      aliasTableName = tableName.getAlias().getName().toUpperCase();
    }

    Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>> realTableTuplePair = firstTuple(realTableName);
    if (realTableTuplePair == null) {
      if (itemMap.get(aliasTableName) == null) {
        itemMap.put(aliasTableName, Tuple.of(Sets.newHashSet(realTableName), Maps.newHashMap()));
      }
      //ignore else
    } else {
      if (itemMap.get(aliasTableName) == null) {
        //for clone
        itemMap.put(aliasTableName, mergeAliasTableTuplePairCollection(Lists.<Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>>newArrayList(realTableTuplePair)));
      } else {
        itemMap.put(aliasTableName, mergeAliasTableTuplePairCollection(Lists.newArrayList(itemMap.get(aliasTableName), realTableTuplePair)));
      }
    }
  }

  //  @SensitiveItemsFinderSub
  @SensitiveItemsFinderPath
  @Override
  public void visit(SubSelect subSelect) {
    //1
    String handleSelectExpressionItemAlias = selectExpressionItemAlias;
    selectExpressionItemAlias = null;
    //2
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> handleItemMap = null;
    if (handleSelectExpressionItemAlias == null) {
      //if not in select expression item, need handle
      handleItemMap = itemMap;
    } else {
      //if in select expression item, will two stack, he he
      selectExpressionItemStack.push(itemMap);
    }
    itemMap = Maps.<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>>newHashMap();

    //process
    if (subSelect.getWithItemsList() != null && subSelect.getWithItemsList().size() > 0) {
      processWithItemsList(subSelect.getWithItemsList());
    }
    subSelect.getSelectBody().accept(this);

    //2
    String aliasTableName = (subSelect.getAlias() != null && subSelect.getAlias().getName() != null) ? subSelect.getAlias().getName().toUpperCase() : null;
    if (Strings.isNullOrEmpty(aliasTableName)) {
      aliasTableName = String0.ASTERISK;
    }
    if (handleSelectExpressionItemAlias != null) {
      handleItemMap = selectExpressionItemStack.pop();
    }
    if (handleItemMap.get(aliasTableName) == null) {
      handleItemMap.put(aliasTableName, mergeAliasTableMap(itemMap));
    } else {
      handleItemMap.put(aliasTableName, mergeAliasTableTuplePairCollection(Lists.newArrayList(mergeAliasTableMap(itemMap), handleItemMap.get(aliasTableName))));
    }
    itemMap = handleItemMap;
    //1
    selectExpressionItemAlias = handleSelectExpressionItemAlias;
    //0: if WithItem not used, will ignore
    if (subSelect.getWithItemsList() != null && subSelect.getWithItemsList().size() > 0) {
      withStack.pop();
    }
  }

  //a+b as c
  @SensitiveItemsFinderTransformed
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
    String aliasTableName = tableColumn.getTable().getFullyQualifiedName();
    if (Strings.isNullOrEmpty(aliasTableName)) {
      throw new UnsupportedOperationException(THE_COLUMN_MUST_BE_LIKE_X_Y);
    }
    aliasTableName = aliasTableName.toUpperCase();

    Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>> aliasTableTuple = firstTuple(aliasTableName);
    if (aliasTableTuple == null) {
      //schema.table.column or schema.table.*
      aliasTableTuple = Tuple.of(Sets.newHashSet(aliasTableName), Maps.newHashMap());
      itemMap.put(aliasTableName, aliasTableTuple);
    }

    Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>> aliasColumnMap = Tuple.getSecond(aliasTableTuple);
    String columnName = tableColumn.getColumnName().toUpperCase();
    Set<Tuple.Quadruple<String, String, String, Boolean>> columnSet = aliasColumnMap.get(columnName);
    if (columnSet == null) {
      //columnName is realColumnName
      aliasColumnMap.put(columnName, Tuple.getFirst(aliasTableTuple).stream().map(realTable -> Tuple.of(realTable, tableColumn.getColumnName().toUpperCase(), Joiner.on(String0.ARROW).join(pathStack), transformed)).collect(Collectors.toSet()));
    } else {
      //columnName is aliasColumnName
      aliasColumnMap.put(columnName, updatePathAndTransformed(columnSet));
    }

    if (!Strings.isNullOrEmpty(selectExpressionItemAlias)) {
      Set<Tuple.Quadruple<String, String, String, Boolean>> aliasColumnSet = aliasColumnMap.get(selectExpressionItemAlias);
      if (aliasColumnSet == null) {
        //(a.b + a.c) as a.d : a.b
        aliasColumnMap.put(selectExpressionItemAlias, Tuple.getFirst(aliasTableTuple).stream().map(realTable -> Tuple.of(realTable, tableColumn.getColumnName().toUpperCase(), Joiner.on(String0.ARROW).join(pathStack), transformed)).collect(Collectors.toSet()));
      } else {
        //(a.b + a.c) as a.d : a.c
        aliasColumnSet.addAll(Tuple.getFirst(aliasTableTuple).stream().map(realTable -> Tuple.of(realTable, tableColumn.getColumnName().toUpperCase(), Joiner.on(String0.ARROW).join(pathStack), transformed)).collect(Collectors.toSet()));
      }
    }
//    if (allowColumnProcessing && tableColumn.getTable() != null && tableColumn.getTable().getName() != null) {
//      visit(tableColumn.getTable());
//    }
  }

  //a/1 as b
  @SensitiveItemsFinderTransformed
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

  //substr(a, 1, 2) as b
  @SensitiveItemsFinderTransformed
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

  @SensitiveItemsFinderTransformed
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

  //a*1 as b
  @SensitiveItemsFinderTransformed
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

  //a-0 as b
  @SensitiveItemsFinderTransformed
  @Override
  public void visit(Subtraction subtraction) {
    visitBinaryExpression(subtraction);
  }

  @Override
  public void visit(NotExpression notExpr) {
    notExpr.getExpression().accept(this);
  }

  @SensitiveItemsFinderTransformed
  @Override
  public void visit(BitwiseRightShift expr) {
    visitBinaryExpression(expr);
  }

  @SensitiveItemsFinderTransformed
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
  //case a when .. then .. else a end as b
  @SensitiveItemsFinderTransformed
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
//    processSubSelect(allComparisonExpression.getSubSelect(), null);
    allComparisonExpression.getSubSelect().accept((ExpressionVisitor) this);
  }

  @Override
  public void visit(AnyComparisonExpression anyComparisonExpression) {
//    processSubSelect(anyComparisonExpression.getSubSelect(), null);
    anyComparisonExpression.getSubSelect().accept((ExpressionVisitor) this);
  }

  @SensitiveItemsFinderAsterisk
  @Override
  public void visit(SubJoin subjoin) {
//    processFromItem(subjoin.getLeft(), subjoin.getAlias());
    subjoin.getLeft().accept(this);
    for (Join join : subjoin.getJoinList()) {
//      processFromItem(join.getRightItem(), null);
      join.getRightItem().accept(this);
    }
  }

  //a||'' as b
  @SensitiveItemsFinderTransformed
  @Override
  public void visit(Concat concat) {
    visitBinaryExpression(concat);
  }

  @Override
  public void visit(Matches matches) {
    visitBinaryExpression(matches);
  }

  @SensitiveItemsFinderTransformed
  @Override
  public void visit(BitwiseAnd bitwiseAnd) {
    visitBinaryExpression(bitwiseAnd);
  }

  @SensitiveItemsFinderTransformed
  @Override
  public void visit(BitwiseOr bitwiseOr) {
    visitBinaryExpression(bitwiseOr);
  }

  @SensitiveItemsFinderTransformed
  @Override
  public void visit(BitwiseXor bitwiseXor) {
    visitBinaryExpression(bitwiseXor);
  }

  //cast(a as string) as b
  @SensitiveItemsFinderTransformed
  @Override
  public void visit(CastExpression cast) {
    cast.getLeftExpression().accept(this);
  }

  //a%1 as b
  @SensitiveItemsFinderTransformed
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

  /**
   * TODO:select sub(t.a,1,3) from (select t1.a from tab1 union select t2.m from tab2) t
   */
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

  @SensitiveItemsFinderAsterisk
  @Override
  public void visit(LateralSubSelect lateralSubSelect) {
//    processSubSelect(lateralSubSelect.getSubSelect(), lateralSubSelect.getAlias());
    lateralSubSelect.getSubSelect().accept((ExpressionVisitor) this);
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
      throw new UnsupportedOperationException(Joiner.on(String0.COLON).join(THE_COLUMN_MUST_BE_LIKE_X_Y, allTableColumns));
    }
    String aliasTableName = allTableColumns.getTable().getFullyQualifiedName();
    if (Strings.isNullOrEmpty(aliasTableName)) {
      throw new UnsupportedOperationException(Joiner.on(String0.COLON).join(THE_COLUMN_MUST_BE_LIKE_X_Y, allTableColumns));
    }
    aliasTableName = aliasTableName.toUpperCase();

    Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>> aliasTableTuple = firstTuple(aliasTableName);
    if (aliasTableTuple == null) {
      //schema.table.*
      aliasTableTuple = Tuple.of(Sets.newHashSet(aliasTableName), Maps.newHashMap());
      itemMap.put(aliasTableName, aliasTableTuple);
    }
    //todo ?
    Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>> aliasColumnMap = Tuple.getSecond(aliasTableTuple);
    Set<Tuple.Quadruple<String, String, String, Boolean>> aliasColumnSet = aliasColumnMap.get(String0.ASTERISK);
    if (aliasColumnSet == null) {
      //columnName is realColumnName
      Set<Tuple.Quadruple<String, String, String, Boolean>> allAliasColumnSet = aliasColumnMap.values().stream().collect(HashSet::new, Set::addAll, Set::addAll);
      if (allAliasColumnSet.size() == 0) {
        aliasColumnMap.put(String0.ASTERISK, Tuple.getFirst(aliasTableTuple).stream().map(realTable -> Tuple.of(realTable, String0.ASTERISK, Joiner.on(String0.ARROW).join(pathStack), transformed)).collect(Collectors.toSet()));
      } else {
        aliasColumnMap.put(String0.ASTERISK, updatePathAndTransformed(allAliasColumnSet));
      }
    } else {
      //columnName is aliasColumnName
      aliasColumnMap.put(String0.ASTERISK, updatePathAndTransformed(aliasColumnSet));
    }
  }

  @SensitiveItemsFinderPath
  @Override
  public void visit(SelectExpressionItem item) {
    String handleSelectExpressionItemAlias = selectExpressionItemAlias;
    selectExpressionItemAlias = (item.getAlias() == null ? String0.EMPTY : (item.getAlias().getName() == null ? String0.EMPTY : item.getAlias().getName().toUpperCase()));
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
      //TODO test.skava.sql.parser.SensitiveItemsFinderTest.testGetItemListFromInsert()
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

  @SensitiveItemsFinderAsterisk
  @Override
  public void visit(ParenthesisFromItem parenthesis) {
//    processFromItem(parenthesis.getFromItem(), parenthesis.getAlias());
    parenthesis.getFromItem().accept(this);
  }
}
