/*
 * @(#)RespMesg.java		Created at 2018/9/7
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.rr;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * https://github.com/ShaneKing/sk-js/blob/mirror/src/RespMesg.js
 */
@Accessors(chain = true)
@ToString(includeFieldNames = true)
public class RespMesg<A> {
  //
  public static final String SUCCESS = "Success";//Just prompt
  public static final String INFO = "Info";//Just prompt
  public static final String WARNING = "Warning";//Business continue, but must prompt
  public static final String ERROR = "Error";//Unknown Exception(done == false), UI will prompt details; Business Stop(done == true), process by component

  @Getter
  @Setter
  private A args;

  //message or expression
  @Getter
  @Setter
  private String code;

  @Getter
  @Setter
  private String type;

  public static <A> RespMesg<A> build(String type, String code, A args) {
    return new RespMesg<A>().setArgs(args).setCode(code).setType(type);
  }

  public static <A> RespMesg<A> failed(String code, A args) {
    return build(ERROR, code, args);
  }

  public static <A> RespMesg<A> failed(String code) {
    return failed(code, null);
  }

  public static <A> RespMesg<A> success(String code, A args) {
    return build(SUCCESS, code, args);
  }

  public static <A> RespMesg<A> success(String code) {
    return success(code, null);
  }

  public boolean successfully() {
    return SUCCESS.equals(type);
  }
}
