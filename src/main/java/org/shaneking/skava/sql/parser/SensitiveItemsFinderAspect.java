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
import net.sf.jsqlparser.statement.select.*;
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
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> handleItemMap = null;
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

  /**
   * pop withItems
   */
  @Around("@annotation(org.shaneking.skava.sql.parser.SensitiveItemsFinderPath)")
  public Object aroundDeep(ProceedingJoinPoint joinPoint) throws Throwable {
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
      }
    }
    Object result = joinPoint.proceed();
    if (sensitiveItemsFinder != null) {
      if (arg0 instanceof Select || arg0 instanceof SelectExpressionItem || arg0 instanceof SubSelect || arg0 instanceof WithItem) {
        sensitiveItemsFinder.getPathStack().pop();
      }
    }
    return result;
  }

  /**
   *
   */
//  @Around("@annotation(org.shaneking.skava.sql.parser.SensitiveItemsFinderSub)")
//  public Object aroundAlias(ProceedingJoinPoint joinPoint) throws Throwable {
//    Object originInstance = joinPoint.getThis();
//    if (originInstance == null) {
//      originInstance = joinPoint.getTarget();
//    }
//    SensitiveItemsFinder sensitiveItemsFinder = null;
//    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> handleItemMap = null;
//    SubSelect subSelect = null;
//    String handleSelectExpressionItemAlias = null;
//    if (originInstance instanceof SensitiveItemsFinder) {
//      sensitiveItemsFinder = (SensitiveItemsFinder) originInstance;
//      Object arg0 = joinPoint.getArgs()[0];
//      if (arg0 instanceof SubSelect) {
//        subSelect = (SubSelect) arg0;
//        handleSelectExpressionItemAlias = sensitiveItemsFinder.getSelectExpressionItemAlias();
//        sensitiveItemsFinder.setSelectExpressionItemAlias(null);
//        handleItemMap = sensitiveItemsFinder.getItemMap();
//        sensitiveItemsFinder.setItemMap(Maps.<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>>newHashMap());
//      }
//    }
//    Object result = joinPoint.proceed();
//    if (sensitiveItemsFinder != null && subSelect != null) {
//      //1
//      sensitiveItemsFinder.setSelectExpressionItemAlias(handleSelectExpressionItemAlias);
//      //2
//      if (handleItemMap != null) {
//        String aliasTableName = (subSelect.getAlias() != null && subSelect.getAlias().getName() != null) ? subSelect.getAlias().getName().toUpperCase() : null;
//        if (Strings.isNullOrEmpty(aliasTableName)) {
//          aliasTableName = String0.ASTERISK;
//        }
//        if (handleItemMap.get(aliasTableName) == null) {
//          handleItemMap.put(aliasTableName, sensitiveItemsFinder.mergeAliasTableMap(sensitiveItemsFinder.getItemMap()));
//        } else {
//          handleItemMap.put(aliasTableName, sensitiveItemsFinder.mergeAliasTableTuplePairCollection(Lists.newArrayList(sensitiveItemsFinder.mergeAliasTableMap(sensitiveItemsFinder.getItemMap()), handleItemMap.get(aliasTableName))));
//        }
//        sensitiveItemsFinder.setItemMap(handleItemMap);
//      }
//      //3
//      if(subSelect.getWithItemsList() != null && subSelect.getWithItemsList().size() > 0){
//        sensitiveItemsFinder.getWithStack().pop();
//      }
//    }
//    return result;
//  }
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

}
