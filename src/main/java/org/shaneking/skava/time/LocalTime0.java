package org.shaneking.skava.time;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.skava.util.Date0;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LocalTime0 {

  @Getter
  private LocalTime localTime = LocalTime.now();

  private LocalTime0(LocalTime localTime) {
    this.localTime = localTime;
  }

  public static LocalTime now() {
    return LocalTime.now();
  }

  public static LocalTime0 on() {
    return LocalTime0.on(LocalTime0.now());
  }

  public static LocalTime0 on(LocalTime localTime) {
    return new LocalTime0(localTime);
  }

  public String format(String pattern) {
    return localTime.format(DateTimeFormatter.ofPattern(pattern));
  }

  public LocalTime0 parse(String pattern, String s) {
    this.localTime = LocalTime.parse(s, DateTimeFormatter.ofPattern(pattern));
    return this;
  }

  public String time() {
    return format(Date0.H_MI_S);
  }
}
