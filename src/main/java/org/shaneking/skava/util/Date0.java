package org.shaneking.skava.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.skava.lang.SkavaException;
import org.shaneking.skava.lang.String0;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Accessors(chain = true)
@Slf4j
@ToString
public class Date0 {
  public static final String H_MI_S = "HH:mm:ss";
  public static final String HMIS = "HHmmss";
  public static final String SSS = "SSS";
  public static final String XXX = "XXX";//+08:00
  public static final String Y_M_D = "yyyy-MM-dd";
  public static final String YMD = "yyyyMMdd";
  public static final String YsMsD = "yyyy/MM/dd";
  public static final String DATE_TIME = Y_M_D + String0.BLANK + H_MI_S;
  public static final String DATE_TIME_SSS = DATE_TIME + String0.DOT + SSS;
  public static final String DATETIME = YMD + HMIS;
  public static final String DATETIMESSS = DATETIME + SSS;

  @Getter
  @Setter
  private Date date = new Date();

  private Date0(Date date) {
    this.date = date;
  }

  public static Date now() {
    return new Date();
  }

  public static Date0 on() {
    return Date0.on(Date0.now());
  }

  public static Date0 on(Date date) {
    return new Date0(date);
  }

  public String date() {
    return format(Y_M_D);
  }

  public String dateTime() {
    return format(DATE_TIME);
  }

  public String dateTimes() {
    return format(DATE_TIME_SSS);
  }

  public String datetime() {
    return format(DATETIME);
  }

  public String datetimes() {
    return format(DATETIMESSS);
  }

  public String format(String pattern) {
    return new SimpleDateFormat(pattern).format(this.getDate());
  }

  public Date0 parse(String pattern, String s) {
    try {
      return this.setDate(new SimpleDateFormat(pattern).parse(s));
    } catch (ParseException e) {
      log.error(e.getMessage(), e);
      throw new SkavaException(e);
    }
  }

  public String time() {
    return format(H_MI_S);
  }

  public String ymd() {
    return format(YMD);
  }

  public String ySmSd() {
    return format(YsMsD);
  }

  public String zone() {
    return format(XXX);
  }

}
