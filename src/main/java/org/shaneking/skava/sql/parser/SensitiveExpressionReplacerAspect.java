/*
 * @(#)SensitiveExpressionReplacerAspect.java		Created at 2018/9/17
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.parser;

import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Stack;

@Aspect
public class SensitiveExpressionReplacerAspect {

  @Around("@annotation(org.shaneking.skava.sql.parser.SensitiveExpressionReplacerPath)")
  public Object aroundPath(ProceedingJoinPoint joinPoint) throws Throwable {
    Stack<String> handlePathStack = null;
    Object originInstance = joinPoint.getThis();
    if (originInstance == null) {
      originInstance = joinPoint.getTarget();
    }
    if (originInstance instanceof SensitiveExpressionReplacer) {
      handlePathStack = ((SensitiveExpressionReplacer) originInstance).getPathStack();
    } else if (originInstance instanceof TableNamesSelectReplacer) {
      ExpressionVisitor expressionVisitor = ((TableNamesSelectReplacer) originInstance).getExpressionVisitor();
      if (expressionVisitor != null && expressionVisitor instanceof SensitiveExpressionReplacer) {
        handlePathStack = ((SensitiveExpressionReplacer) expressionVisitor).getPathStack();
      }
    } else if (originInstance instanceof SensitiveStatementReplacer) {
      ExpressionVisitor expressionVisitor = ((SensitiveStatementReplacer) originInstance).getExpressionReplacer();
      if (expressionVisitor != null && expressionVisitor instanceof SensitiveExpressionReplacer) {
        handlePathStack = ((SensitiveExpressionReplacer) expressionVisitor).getPathStack();
      }
    }
    Object arg0 = joinPoint.getArgs()[0];
    if (handlePathStack != null) {
      if (arg0 instanceof Select) {
        handlePathStack.push(SensitiveItemsFinder.PATH_OF_SELECT);
      } else if (arg0 instanceof SelectExpressionItem) {
        handlePathStack.push(SensitiveItemsFinder.PATH_OF_SELECT_EXPRESSION_ITEM);
      } else if (arg0 instanceof SubSelect) {
        handlePathStack.push(SensitiveItemsFinder.PATH_OF_SUB_SELECT);
      } else if (arg0 instanceof WithItem) {
        handlePathStack.push(SensitiveItemsFinder.PATH_OF_WITH_ITEM);
      }
    }
    Object result = joinPoint.proceed();
    if (handlePathStack != null) {
      if (arg0 instanceof Select || arg0 instanceof SelectExpressionItem || arg0 instanceof SubSelect || arg0 instanceof WithItem) {
        handlePathStack.pop();
      }
    }
    return result;
  }
}
