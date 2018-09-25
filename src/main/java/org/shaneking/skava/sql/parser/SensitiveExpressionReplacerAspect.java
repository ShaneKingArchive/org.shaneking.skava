/*
 * @(#)SensitiveExpressionReplacerAspect.java		Created at 2018/9/17
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.parser;

import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.truncate.Truncate;
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
      } else if (arg0 instanceof SubJoin) {
        handlePathStack.push(SensitiveItemsFinder.PATH_OF_FROM_ITEM);
      } else if (arg0 instanceof LateralSubSelect) {
        handlePathStack.push(SensitiveItemsFinder.PATH_OF_FROM_ITEM);
      } else if (arg0 instanceof ParenthesisFromItem) {
        handlePathStack.push(SensitiveItemsFinder.PATH_OF_FROM_ITEM);
      } else if (arg0 instanceof Insert) {
        handlePathStack.push(SensitiveItemsFinder.PATH_OF_INSERT);
      } else if (arg0 instanceof Truncate) {
        handlePathStack.push(SensitiveItemsFinder.PATH_OF_TRUNCATE);
      }
    }
    Object result = joinPoint.proceed();
    if (handlePathStack != null) {
      if (arg0 instanceof Select || arg0 instanceof SelectExpressionItem || arg0 instanceof SubSelect || arg0 instanceof WithItem || arg0 instanceof SubJoin || arg0 instanceof LateralSubSelect || arg0 instanceof ParenthesisFromItem || arg0 instanceof Insert || arg0 instanceof Truncate) {
        handlePathStack.pop();
      }
    }
    return result;
  }
}
