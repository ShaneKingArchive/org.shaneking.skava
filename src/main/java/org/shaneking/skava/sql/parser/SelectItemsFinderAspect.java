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

  //  @Around("@annotation(com.test.YourAnnotation) && execution(* *(..))")
  @Around("execution(* org.shaneking.skava.sql.parser.SelectItemsFinder.visit(..))")
  public Object aroundVisit(ProceedingJoinPoint joinPoint) throws Throwable {
    Object originInstance = joinPoint.getThis();
    if (originInstance == null) {
      originInstance = joinPoint.getTarget();
    }
    SelectItemsFinder selectItemsFinder = null;
    if (originInstance instanceof SelectItemsFinder) {
      selectItemsFinder = (SelectItemsFinder) originInstance;
      selectItemsFinder.getStack().push(joinPoint.getArgs()[0]);
    }
    System.out.println(selectItemsFinder.getStack());
    Object result = joinPoint.proceed();
    if (originInstance != null) {
      selectItemsFinder.getStack().pop();
    }
    return result;
  }
}
