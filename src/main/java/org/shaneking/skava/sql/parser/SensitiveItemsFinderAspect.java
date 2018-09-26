/*
 * @(#)SelectItemsFinderAspect.java		Created at 2018/9/15
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.parser;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.truncate.Truncate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.ling.lang.String0;

import java.util.Map;
import java.util.Set;

@Aspect
public class SensitiveItemsFinderAspect {

  //    @Around("execution(* org.shaneking.skava.sql.parser.SensitiveItemsFinder.visit(..))")
  //0
  @Around("@annotation(org.shaneking.skava.sql.parser.SensitiveItemsFinderTransformed)")
  public Object aroundTransformed(ProceedingJoinPoint joinPoint) throws Throwable {
    Boolean handleTransformed = null;
    Object originInstance = joinPoint.getThis();
    if (originInstance == null) {
      originInstance = joinPoint.getTarget();
    }
    SensitiveItemsFinder sensitiveItemsFinder = null;
    if (originInstance instanceof SensitiveItemsFinder) {
      sensitiveItemsFinder = (SensitiveItemsFinder) originInstance;
      handleTransformed = sensitiveItemsFinder.isTransformed();
      sensitiveItemsFinder.setTransformed(true);
    }
    Object result = joinPoint.proceed();
    if (sensitiveItemsFinder != null) {
      sensitiveItemsFinder.setTransformed(handleTransformed);
    }
    return result;
  }

  //0
  @Around("@annotation(org.shaneking.skava.sql.parser.SensitiveItemsFinderAlias)")
  public Object aroundAlias(ProceedingJoinPoint joinPoint) throws Throwable {
    String handleSelectExpressionItem = null;
    Object originInstance = joinPoint.getThis();
    if (originInstance == null) {
      originInstance = joinPoint.getTarget();
    }
    SensitiveItemsFinder sensitiveItemsFinder = null;
    if (originInstance instanceof SensitiveItemsFinder) {
      sensitiveItemsFinder = (SensitiveItemsFinder) originInstance;
      handleSelectExpressionItem = sensitiveItemsFinder.getSelectExpressionItemAlias();
      sensitiveItemsFinder.setSelectExpressionItemAlias(null);
    }
    Object result = joinPoint.proceed();
    if (sensitiveItemsFinder != null) {
      sensitiveItemsFinder.setSelectExpressionItemAlias(handleSelectExpressionItem);
    }
    return result;
  }

  //1
  @Around("@annotation(org.shaneking.skava.sql.parser.SensitiveItemsFinderPath)")
  public Object aroundPath(ProceedingJoinPoint joinPoint) throws Throwable {
    Object originInstance = joinPoint.getThis();
    if (originInstance == null) {
      originInstance = joinPoint.getTarget();
    }
    SensitiveItemsFinder sensitiveItemsFinder = null;
    Object arg0 = joinPoint.getArgs()[0];
    if (originInstance instanceof SensitiveItemsFinder) {
      sensitiveItemsFinder = (SensitiveItemsFinder) originInstance;
      if (arg0 instanceof Select) {
        sensitiveItemsFinder.getPathStack().push(SensitiveItemsFinder.PATH_OF_SELECT);
      } else if (arg0 instanceof SelectExpressionItem) {
        sensitiveItemsFinder.getPathStack().push(SensitiveItemsFinder.PATH_OF_SELECT_EXPRESSION_ITEM);
      } else if (arg0 instanceof SubSelect) {
        sensitiveItemsFinder.getPathStack().push(SensitiveItemsFinder.PATH_OF_SUB_SELECT);
      } else if (arg0 instanceof WithItem) {
        sensitiveItemsFinder.getPathStack().push(SensitiveItemsFinder.PATH_OF_WITH_ITEM);
      } else if (arg0 instanceof SubJoin) {
        sensitiveItemsFinder.getPathStack().push(SensitiveItemsFinder.PATH_OF_FROM_ITEM);
      } else if (arg0 instanceof LateralSubSelect) {
        sensitiveItemsFinder.getPathStack().push(SensitiveItemsFinder.PATH_OF_FROM_ITEM);
      } else if (arg0 instanceof ParenthesisFromItem) {
        sensitiveItemsFinder.getPathStack().push(SensitiveItemsFinder.PATH_OF_FROM_ITEM);
      } else if (arg0 instanceof Insert) {
        sensitiveItemsFinder.getPathStack().push(SensitiveItemsFinder.PATH_OF_INSERT);
      } else if (arg0 instanceof Truncate) {
        sensitiveItemsFinder.getPathStack().push(SensitiveItemsFinder.PATH_OF_TRUNCATE);
      }
    }
    Object result = joinPoint.proceed();
    if (sensitiveItemsFinder != null) {
      if (arg0 instanceof Select || arg0 instanceof SelectExpressionItem || arg0 instanceof SubSelect || arg0 instanceof WithItem || arg0 instanceof SubJoin || arg0 instanceof LateralSubSelect || arg0 instanceof ParenthesisFromItem || arg0 instanceof Insert || arg0 instanceof Truncate) {
        sensitiveItemsFinder.getPathStack().pop();
      }
    }
    return result;
  }

  //2
  @Around("@annotation(org.shaneking.skava.sql.parser.SensitiveItemsFinderAsterisk)")
  public Object aroundAsterisk(ProceedingJoinPoint joinPoint) throws Throwable {
    Object originInstance = joinPoint.getThis();
    if (originInstance == null) {
      originInstance = joinPoint.getTarget();
    }
    SensitiveItemsFinder sensitiveItemsFinder = null;
    if (originInstance instanceof SensitiveItemsFinder) {
      sensitiveItemsFinder = (SensitiveItemsFinder) originInstance;
    }
    Object arg0 = joinPoint.getArgs()[0];
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> handleItemMap = null;
    if (arg0 instanceof SubJoin || arg0 instanceof LateralSubSelect || arg0 instanceof ParenthesisFromItem) {
      handleItemMap = sensitiveItemsFinder.getItemMap();
      sensitiveItemsFinder.setItemMap(Maps.newHashMap());
    }
    Object result = joinPoint.proceed();
    if (sensitiveItemsFinder != null && handleItemMap != null) {
      Alias alias = null;
      if (arg0 instanceof SubJoin) {
        alias = ((SubJoin) arg0).getAlias();
      } else if (arg0 instanceof LateralSubSelect) {
        alias = ((LateralSubSelect) arg0).getAlias();
      } else if (arg0 instanceof ParenthesisFromItem) {
        alias = ((ParenthesisFromItem) arg0).getAlias();
      }
      if (alias != null && !Strings.isNullOrEmpty(alias.getName())) {
        handleItemMap.put(alias.getName().toUpperCase(), sensitiveItemsFinder.mergeAliasTableMap(sensitiveItemsFinder.getItemMap()));
      } else {
        handleItemMap.put(String0.ASTERISK, sensitiveItemsFinder.mergeAliasTableMap(sensitiveItemsFinder.getItemMap()));
      }
      sensitiveItemsFinder.setItemMap(handleItemMap);
    }
    return result;
  }

}
