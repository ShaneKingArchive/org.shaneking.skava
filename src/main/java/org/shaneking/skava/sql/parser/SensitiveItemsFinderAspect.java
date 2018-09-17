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
public class SensitiveItemsFinderAspect {

  //    @Around("execution(* org.shaneking.skava.sql.parser.SensitiveItemsFinder.visit(..))")
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
    }
    if (sensitiveItemsFinder != null) {
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
