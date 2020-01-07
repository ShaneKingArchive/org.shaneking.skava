package org.shaneking.skava.time;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.skava.util.Date0;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LocalDate0 {

  @Getter
  private LocalDate localDate = LocalDate.now();

  private LocalDate0(LocalDate localDate) {
    this.localDate = localDate;
  }

  public static LocalDate now() {
    return LocalDate.now();
  }

  public static LocalDate0 on() {
    return LocalDate0.on(LocalDate0.now());
  }

  public static LocalDate0 on(LocalDate localDate) {
    return new LocalDate0(localDate);
  }

  public String date() {
    return format(Date0.Y_M_D);
  }

  public String format(String pattern) {
    return localDate.format(DateTimeFormatter.ofPattern(pattern));
  }

  public LocalDate0 parse(String pattern, String s) {
    this.localDate = LocalDate.parse(s, DateTimeFormatter.ofPattern(pattern));
    return this;
  }

  public String ymd() {
    return format(Date0.YMD);
  }

  public String ySmSd() {
    return format(Date0.YsMsD);
  }
}
