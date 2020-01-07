package org.shaneking.skava.time;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.skava.util.Date0;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ZonedDateTime0 {

  @Getter
  private ZonedDateTime zonedDateTime = ZonedDateTime.now();

  private ZonedDateTime0(ZonedDateTime zonedDateTime) {
    this.zonedDateTime = zonedDateTime;
  }

  public static ZonedDateTime now() {
    return ZonedDateTime.now();
  }

  public static ZonedDateTime0 on() {
    return ZonedDateTime0.on(ZonedDateTime0.now());
  }

  public static ZonedDateTime0 on(ZonedDateTime zonedDateTime) {
    return new ZonedDateTime0(zonedDateTime);
  }

  public String date() {
    return format(Date0.Y_M_D);
  }

  public String dateTime() {
    return format(Date0.DATE_TIME);
  }

  public String dateTimes() {
    return format(Date0.DATE_TIME_SSS);
  }

  public String datetime() {
    return format(Date0.DATETIME);
  }

  public String datetimes() {
    return format(Date0.DATETIMESSS);
  }

  public String format(String pattern) {
    return zonedDateTime.format(DateTimeFormatter.ofPattern(pattern));
  }

  public ZonedDateTime0 parse(String pattern, String s) {
    this.zonedDateTime = ZonedDateTime.parse(s, DateTimeFormatter.ofPattern(pattern));
    return this;
  }

  public String time() {
    return format(Date0.H_MI_S);
  }

  public String ymd() {
    return format(Date0.YMD);
  }

  public String ySmSd() {
    return format(Date0.YsMsD);
  }

  public String zone() {
    return format(Date0.XXX);
  }
}
