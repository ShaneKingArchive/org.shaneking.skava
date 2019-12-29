package org.shaneking.skava.rr;

public class Ctx {
  public static final ThreadLocal<String> LANGUAGE = new ThreadLocal<String>();//default zh-CN
  public static final ThreadLocal<String> USER_ID = new ThreadLocal<String>();
}
