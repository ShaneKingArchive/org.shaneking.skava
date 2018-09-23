/*
 * @(#)SKTable.java		Created at 2017/9/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.annotation;

import org.shaneking.skava.ling.lang.String0;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @see javax.persistence.Table
 */
@Target(TYPE)
@Retention(RUNTIME)
@Inherited
public @interface SKTable {

  /**
   * (Optional) The name of the table.
   * <p> Defaults to the entity name.
   */
  String name() default String0.EMPTY;

  /**
   * (Optional) The schema of the table.
   * <p> Defaults to the default schema for user.
   */
  String schema() default String0.EMPTY;

}
