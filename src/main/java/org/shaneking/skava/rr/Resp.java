/*
 * @(#)Resp.java		Created at 2018/9/7
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
 * https://github.com/ShaneKing/sk-js/blob/mirror/src/Resp.js
 */
@Accessors(chain = true)
@ToString
public class Resp<D> {
  //
  public static final String SUCCESS = "Success";//Just prompt
  public static final String INFO = "Info";//Just prompt
  public static final String WARNING = "Warning";//Business continue, but must prompt
  public static final String ERROR = "Error";//Unknown Exception(done == false), UI will prompt details; Business Stop(done == true), process by component

  @Getter
  @Setter
  private D data;//Business Object

  @Getter
  @Setter
  private boolean done;//true: No Unknown Exception,false: has Unknown Exception

  @Getter
  @Setter
  private String mesg;//Result Message Object, Required if done is false

  @Getter
  @Setter
  private String type;

  public static <D> Resp<D> build(D data, boolean done, String mesg, String type) {
    return new Resp<D>().setData(data).setDone(done).setMesg(mesg).setType(type);
  }

  public static <D> Resp<D> failed(String mesg, String type, D data) {
    return build(data, false, mesg, type);
  }

  public static <D> Resp<D> failed(String mesg, String type) {
    return failed(mesg, type, null);
  }

  public static <D> Resp<D> success(D data) {
    return build(data, true, null, null);
  }
}
