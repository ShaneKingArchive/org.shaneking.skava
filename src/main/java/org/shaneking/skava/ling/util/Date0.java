/*
 * @(#)Date0.java		Created at 16/3/26
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.ling.util;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Date0
{
  public static final String Y_M_D     = "yyyy-MM-dd";
  public static final String YMD       = "yyyyMMdd";
  public static final String H_MI_S    = "HH:mm:ss";
  public static final String HMIS      = "HHmmss";
  public static final String XXX       = "XXX";
  public static final String DATE_TIME = Y_M_D + " " + H_MI_S;
  public static final String DATETIME  = YMD + HMIS;

  public static String date()
  {
    return toString(now(), Y_M_D);
  }

  public static String time()
  {
    return toString(now(), H_MI_S);
  }

  public static String zone()
  {
    return toString(now(), XXX);
  }

  public static String dateTime()
  {
    return toString(now(), DATE_TIME);
  }

  public static String datetime()
  {
    return toString(now(), DATETIME);
  }

  public static Date now()
  {
    return new Date();
  }

  public static String toString(@Nonnull Date date, @Nonnull String pattern)
  {
    return new SimpleDateFormat(pattern).format(date);
  }

}
