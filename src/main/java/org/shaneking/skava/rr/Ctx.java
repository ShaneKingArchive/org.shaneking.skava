package org.shaneking.skava.rr;

public class Ctx {
  //default zh-CN, ref: http://www.rfc-editor.org/rfc/bcp/bcp47.txt
  public static final ThreadLocal<String> LANGUAGE = new ThreadLocal<String>();
  public static final ThreadLocal<String> USER_ID = new ThreadLocal<String>();
}
