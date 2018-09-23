/*
 * @(#)TableNamesReplacer.java		Created at 2018/9/17
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.parser;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.MySQLIndexHint;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Pivot;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

import java.util.Map;

public class TableNamesSelectReplacer extends SelectDeParser {

  //Map<SCHEMA.TABLE_NAME, (select a.* from SCHEMA.TABLE_NAME a ...)>
  private Map<String, String> tableMap = Maps.newHashMap();

  TableNamesSelectReplacer(Map<String, String> tableMap) {
    super();
    this.tableMap = tableMap;
  }

  TableNamesSelectReplacer(ExpressionVisitor expressionVisitor, StringBuilder buffer, Map<String, String> tableMap) {
    super(expressionVisitor, buffer);
    this.tableMap = tableMap;
  }

  @Override
  public void visit(Table tableName) {
    String fullTableName = tableName.getFullyQualifiedName();
    String replaceString = tableMap.get(fullTableName.toUpperCase());
    getBuffer().append(Strings.isNullOrEmpty(replaceString) ? tableName.getFullyQualifiedName() : replaceString);
//    buffer.append(tableName.getFullyQualifiedName());
    Alias alias = tableName.getAlias();
    if (alias != null) {
      getBuffer().append(alias);
    }
    Pivot pivot = tableName.getPivot();
    if (pivot != null) {
      pivot.accept(this);
    }
    MySQLIndexHint indexHint = tableName.getIndexHint();
    if (indexHint != null) {
      getBuffer().append(indexHint);
    }
//    super.visit(tableName);
  }

  @SensitiveExpressionReplacerPath
  @Override
  public void visit(SubSelect subSelect) {
    super.visit(subSelect);
  }

  @SensitiveExpressionReplacerPath
  @Override
  public void visit(WithItem withItem) {
    super.visit(withItem);
  }

  @SensitiveExpressionReplacerPath
  @Override
  public void visit(SelectExpressionItem selectExpressionItem) {
    super.visit(selectExpressionItem);
  }
}
