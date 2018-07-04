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

  private Date date = new Date();

  private Date0(Date date)
  {
    this.date = date;
  }

  public static Date now()
  {
    return new Date();
  }

  public static Date0 on()
  {
    return Date0.on(Date0.now());
  }

  public static Date0 on(Date date)
  {
    return new Date0(date);
  }

  public String date()
  {
    return format(Y_M_D);
  }

  public String time()
  {
    return format(H_MI_S);
  }

  public String zone()
  {
    return format(XXX);
  }

  public String dateTime()
  {
    return format(DATE_TIME);
  }

  public String datetime()
  {
    return format(DATETIME);
  }

  public String format(@Nonnull String pattern)
  {
    return new SimpleDateFormat(pattern).format(date);
  }

}
