/*
 * @(#)SensitiveItemsExpressionReplacer.java		Created at 2018/9/17
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.parser;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NonNull;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.ling.lang.String0;

import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class SensitiveExpressionReplacer extends ExpressionDeParser {
  private static final String THE_COLUMN_MUST_BE_LIKE_X_Y = "The column must be like x.y";

  /**
   * the first item, need init out of this class
   */
  @Getter
  private Stack<String> pathStack;

  //Map<TABLE.COLUMN, Tuple.Triple<PATH, HASH(, )>>
  @Getter
  private Map<String, Tuple.Triple<Set<String>, String, String>> itemMap = Maps.newHashMap();

  SensitiveExpressionReplacer(@NonNull Stack<String> pathStack, Map<String, Tuple.Triple<Set<String>, String, String>> itemMap) {
    super();
    this.pathStack = pathStack;
    this.itemMap = itemMap;
  }

  @SensitiveExpressionReplacerPath
  @Override
  public void visit(SubSelect subSelect) {
    super.visit(subSelect);
  }

  @Override
  public void visit(Column tableColumn) {
    final Table table = tableColumn.getTable();
    String tableName = null;
    if (table != null) {
      if (table.getAlias() != null) {
        tableName = table.getAlias().getName();
      } else {
        tableName = table.getFullyQualifiedName();
      }
    }

//    if (tableName != null && !tableName.isEmpty()) {
//      buffer.append(tableName).append(".");
//    }
//
//    buffer.append(tableColumn.getColumnName());

    if (Strings.isNullOrEmpty(tableName)) {
      throw new UnsupportedOperationException(THE_COLUMN_MUST_BE_LIKE_X_Y);
    }

    String fullColumnName = tableName + "." + tableColumn.getColumnName();
    Tuple.Triple<Set<String>, String, String> replaceTuplePair = itemMap.get(fullColumnName.toUpperCase());
    if (replaceTuplePair != null && Tuple.getFirst(replaceTuplePair) != null && Tuple.getFirst(replaceTuplePair).contains(Joiner.on(String0.ARROW).join(pathStack))) {
      getBuffer().append(Tuple.getSecond(replaceTuplePair)).append(fullColumnName).append(Tuple.getThird(replaceTuplePair));
    } else {
      getBuffer().append(fullColumnName);
    }
  }
}
