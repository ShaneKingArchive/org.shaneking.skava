/*
 * @(#)TablesNamesFinder.java		Created at 2018/9/11
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.parser;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.comment.Comment;
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
import net.sf.jsqlparser.statement.values.ValuesStatement;
import org.shaneking.skava.ling.collect.Tuple;

import java.util.Set;

/**
 * Find all used tables within an select statement.
 * <p>
 * Override extractTableName method to modify the extracted table names (e.g. without schema).
 */
public class TableNamesFinder implements SelectVisitor, FromItemVisitor, ExpressionVisitor, ItemsListVisitor, SelectItemVisitor, StatementVisitor {

  private static final String NOT_SUPPORTED_STATEMENT_TYPE_YET = "Not supported statement type yet";
  //Tuple.Pair<SCHEMA.TABLE, ALIAS>
  private Set<Tuple.Pair<String, String>> tables;
  //select table.column from table; the table is tableName in Column instance
  //select t.column from table t; the t is tableName in Column instance too
  private boolean allowColumnProcessing = false;

  /**
   * There are special names, that are not table names but are parsed as tables. These names are
   * collected here and are not included in the tables - names anymore.
   */
  private Set<String> otherItemNames;

  //Main entry for this Tool class. A list of found tables is returned.
  public Set<Tuple.Pair<String, String>> findTableList(Expression expr) {
    init(true);
    expr.accept(this);
    return tables;
  }

  //Main entry for this Tool class. A list of found tables is returned.
  public Set<Tuple.Pair<String, String>> findTableList(Statement statement) {
    init(false);
    statement.accept(this);
    return tables;
  }

  //Override to adapt the tableName generation (e.g. with / without schema).
  private String extractTableName(Table table) {
    return table.getFullyQualifiedName();
  }

  /**
   * Initializes table names collector. Important is the usage of Column instances to find table
   * names. This is only allowed for expression parsing, where a better place for tablenames could
   * not be there. For complete statements only from items are used to avoid some alias as
   * tablenames.
   */
  private void init(boolean allowColumnProcessing) {
    otherItemNames = Sets.newHashSet();
    tables = Sets.newHashSet();
    this.allowColumnProcessing = allowColumnProcessing;
  }

  private void visitBinaryExpression(BinaryExpression binaryExpression) {
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
    select.getSelectBody().accept(this);
  }

  @Override
  public void visit(WithItem withItem) {
    otherItemNames.add(withItem.getName().toLowerCase());
    withItem.getSelectBody().accept(this);
  }

  @Override
  public void visit(PlainSelect plainSelect) {
    if (plainSelect.getSelectItems() != null) {
      for (SelectItem item : plainSelect.getSelectItems()) {
        item.accept(this);
      }
    }

    if (plainSelect.getFromItem() != null) {
      plainSelect.getFromItem().accept(this);
    }

    if (plainSelect.getJoins() != null) {
      for (Join join : plainSelect.getJoins()) {
        join.getRightItem().accept(this);
      }
    }
    if (plainSelect.getWhere() != null) {
      plainSelect.getWhere().accept(this);
    }

    if (plainSelect.getHaving() != null) {
      plainSelect.getHaving().accept(this);
    }

    if (plainSelect.getOracleHierarchical() != null) {
      plainSelect.getOracleHierarchical().accept(this);
    }
  }

  @Override
  public void visit(Table tableName) {
    String tableWholeName = extractTableName(tableName);
    if (!otherItemNames.contains(tableWholeName.toLowerCase())) {
//      && !tables.contains(tableWholeName)) {
      tables.add(Tuple.of(tableWholeName.toUpperCase(), tableName.getAlias() == null ? null : Strings.nullToEmpty(tableName.getAlias().getName()).toUpperCase()));
    }
  }

  @Override
  public void visit(SubSelect subSelect) {
    if (subSelect.getWithItemsList() != null) {
      for (WithItem withItem : subSelect.getWithItemsList()) {
        withItem.accept(this);
      }
    }
    subSelect.getSelectBody().accept(this);
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
    if (allowColumnProcessing && tableColumn.getTable() != null && tableColumn.getTable().getName() != null) {
//      visit(tableColumn.getTable());
      tableColumn.getTable().accept(this);
    }
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
//      visit(exprList);
      exprList.accept(this);
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
    for (Expression expression : expressionList.getExpressions()) {
      expression.accept(this);
    }
  }

  @Override
  public void visit(NamedExpressionList namedExpressionList) {
    for (Expression expression : namedExpressionList.getExpressions()) {
      expression.accept(this);
    }
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
    allComparisonExpression.getSubSelect().getSelectBody().accept(this);
  }

  @Override
  public void visit(AnyComparisonExpression anyComparisonExpression) {
    anyComparisonExpression.getSubSelect().getSelectBody().accept(this);
  }

  @Override
  public void visit(SubJoin subjoin) {
    subjoin.getLeft().accept(this);
    for (Join join : subjoin.getJoinList()) {
      join.getRightItem().accept(this);
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
  }

  @Override
  public void visit(SetOperationList list) {
    for (SelectBody plainSelect : list.getSelects()) {
      plainSelect.accept(this);
    }
  }

  @Override
  public void visit(ExtractExpression eexpr) {
  }

  @Override
  public void visit(LateralSubSelect lateralSubSelect) {
    lateralSubSelect.getSubSelect().getSelectBody().accept(this);
  }

  @Override
  public void visit(MultiExpressionList multiExprList) {
    for (ExpressionList exprList : multiExprList.getExprList()) {
      exprList.accept(this);
    }
  }

  @Override
  public void visit(ValuesList valuesList) {
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
  }

  @Override
  public void visit(AllTableColumns allTableColumns) {
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
//    visit(delete.getTable());
    delete.getTable().accept(this);
    if (delete.getJoins() != null) {
      for (Join join : delete.getJoins()) {
        join.getRightItem().accept(this);
      }
    }

    if (delete.getWhere() != null) {
      delete.getWhere().accept(this);
    }
  }

  @Override
  public void visit(Update update) {
    for (Table table : update.getTables()) {
//      visit(table);
      table.accept(this);
    }
    if (update.getExpressions() != null) {
      for (Expression expression : update.getExpressions()) {
        expression.accept(this);
      }
    }

    if (update.getFromItem() != null) {
      update.getFromItem().accept(this);
    }

    if (update.getJoins() != null) {
      for (Join join : update.getJoins()) {
        join.getRightItem().accept(this);
      }
    }

    if (update.getWhere() != null) {
      update.getWhere().accept(this);
    }
  }

  @Override
  public void visit(Insert insert) {
//    visit(insert.getTable());
    insert.getTable().accept(this);
    if (insert.getItemsList() != null) {
      insert.getItemsList().accept(this);
    }
    if (insert.getSelect() != null) {
//      visit(insert.getSelect());
      insert.getSelect().accept(this);
    }
  }

  @Override
  public void visit(Replace replace) {
//    visit(replace.getTable());
    replace.getTable().accept(this);
    if (replace.getExpressions() != null) {
      for (Expression expression : replace.getExpressions()) {
        expression.accept(this);
      }
    }
    if (replace.getItemsList() != null) {
      replace.getItemsList().accept(this);
    }
  }

  @Override
  public void visit(Drop drop) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
  }

  @Override
  public void visit(Truncate truncate) {
    truncate.getTable().accept(this);
  }

  @Override
  public void visit(CreateIndex createIndex) {
    throw new UnsupportedOperationException(NOT_SUPPORTED_STATEMENT_TYPE_YET);
  }

  @Override
  public void visit(CreateTable create) {
//    visit(create.getTable());
    create.getTable().accept(this);
    if (create.getSelect() != null) {
      create.getSelect().accept(this);
    }
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
  public void visit(ShowStatement set) {
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
//    visit(merge.getTable());
    merge.getTable().accept(this);
    if (merge.getUsingTable() != null) {
      merge.getUsingTable().accept(this);
    } else if (merge.getUsingSelect() != null) {
      merge.getUsingSelect().accept((FromItemVisitor) this);
    }
  }

  @Override
  public void visit(OracleHint hint) {
  }

  @Override
  public void visit(TableFunction valuesList) {
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

  }

  @Override
  public void visit(Upsert upsert) {
//    visit(upsert.getTable());
    upsert.getTable().accept(this);
    if (upsert.getItemsList() != null) {
      upsert.getItemsList().accept(this);
    }
    if (upsert.getSelect() != null) {
//      visit(upsert.getSelect());
      upsert.getSelect().accept(this);
    }
  }

  @Override
  public void visit(UseStatement use) {
  }

  @Override
  public void visit(ParenthesisFromItem parenthesis) {
    parenthesis.getFromItem().accept(this);
  }

  @Override
  public void visit(Block block) {
    if (block.getStatements() != null) {
      visit(block.getStatements());
    }
  }


  @Override
  public void visit(Comment comment) {
    if (comment.getTable() != null) {
      visit(comment.getTable());
    }
    if (comment.getColumn() != null) {
      Table table = comment.getColumn().getTable();
      if (table != null) {
        visit(table);
      }
    }
  }

  @Override
  public void visit(ValuesStatement values) {
    for (Expression expr : values.getExpressions()) {
      expr.accept(this);
    }
  }
}
