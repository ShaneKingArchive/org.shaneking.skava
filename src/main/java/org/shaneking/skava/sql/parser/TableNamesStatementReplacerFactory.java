/*
 * @(#)TableNamesStatementReplacer.java		Created at 2018/9/23
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.parser;

import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

import java.util.Map;

public class TableNamesStatementReplacerFactory {

  public static StatementDeParser create(StringBuilder stringBuilder, Map<String, String> tableMap) {
    ExpressionDeParser expressionDeParser = new ExpressionDeParser();
    TableNamesSelectReplacer tableNamesSelectReplacer = new TableNamesSelectReplacer(expressionDeParser, stringBuilder, tableMap);
    expressionDeParser.setSelectVisitor(tableNamesSelectReplacer);
    expressionDeParser.setBuffer(stringBuilder);
    return new StatementDeParser(expressionDeParser, tableNamesSelectReplacer, stringBuilder);
  }

}
