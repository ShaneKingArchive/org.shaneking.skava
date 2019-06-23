/*
 * @(#)Resp.java		Created at 2018/9/7
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sk.rr;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * https://github.com/ShaneKing/sk-js/blob/mirror/src/Resp.js
 */
@Accessors(chain = true)
@ToString
public class Resp<D> {

  public static final String CODE_UNKNOWN_EXCEPTION = "-1";
  public static final String CODE_SUCCESSFULLY = "0";
  public static final String CODE_KNOWN_EXCEPTION = "1";//1+

  @Getter
  @Setter
  private String code;

  @Getter
  @Setter
  private D data;//Business Object

  @Getter
  @Setter
  private String mesg;//Required if code is not 0

  public static <D> Resp<D> build(String code, D data, String mesg) {
    return new Resp<D>().setCode(code).setData(data).setMesg(mesg);
  }

  public static <D> Resp<D> failed(String code, String mesg, D data) {
    return build(code, data, mesg);
  }

  public static <D> Resp<D> failed(String code, String mesg) {
    return failed(code, mesg, null);
  }

  public static <D> Resp<D> failed(String code) {
    return failed(Resp.CODE_KNOWN_EXCEPTION, Resp.CODE_KNOWN_EXCEPTION);
  }

  public static <D> Resp<D> failed() {
    return failed(Resp.CODE_UNKNOWN_EXCEPTION, Resp.CODE_UNKNOWN_EXCEPTION);
  }

  public static <D> Resp<D> success(D data) {
    return build(Resp.CODE_SUCCESSFULLY, data, null);
  }
}