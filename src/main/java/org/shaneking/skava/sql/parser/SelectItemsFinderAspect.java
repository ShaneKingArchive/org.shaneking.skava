/*
 * @(#)SelectItemsFinderAspect.java		Created at 2018/9/15
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.parser;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class SelectItemsFinderAspect {

  //  @Around("execution(* org.shaneking.skava.sql.parser.SelectItemsFinder.visit(..))")
  @Around("@annotation(org.shaneking.skava.sql.parser.SelectItemsFinderTransformed)")
  public Object aroundTransformed(ProceedingJoinPoint joinPoint) throws Throwable {
    Boolean handleTransformed = null;
    Object originInstance = joinPoint.getThis();
    if (originInstance == null) {
      originInstance = joinPoint.getTarget();
    }
    SelectItemsFinder selectItemsFinder = null;
    if (originInstance instanceof SelectItemsFinder) {
      selectItemsFinder = (SelectItemsFinder) originInstance;
    }
    if (selectItemsFinder != null) {
      handleTransformed = selectItemsFinder.isTransformed();
      selectItemsFinder.setTransformed(true);
    }
    Object result = joinPoint.proceed();
    if (selectItemsFinder != null) {
      selectItemsFinder.setTransformed(handleTransformed);
    }
    return result;
  }
}
