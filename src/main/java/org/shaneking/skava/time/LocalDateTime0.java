package org.shaneking.skava.time;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.skava.util.Date0;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LocalDateTime0 {

  @Getter
  private LocalDateTime localDateTime = LocalDateTime.now();

  private LocalDateTime0(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }

  public static LocalDateTime now() {
    return LocalDateTime.now();
  }

  public static LocalDateTime0 on() {
    return LocalDateTime0.on(LocalDateTime0.now());
  }

  public static LocalDateTime0 on(LocalDateTime localDateTime) {
    return new LocalDateTime0(localDateTime);
  }

  public String date() {
    return format(Date0.Y_M_D);
  }

  public String dateTime() {
    return format(Date0.DATE_TIME);
  }

  public String datetime() {
    return format(Date0.DATETIME);
  }

  public String format(String pattern) {
    return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
  }

  public LocalDateTime0 parse(String pattern, String s) {
    this.localDateTime = LocalDateTime.parse(s, DateTimeFormatter.ofPattern(pattern));
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
}
