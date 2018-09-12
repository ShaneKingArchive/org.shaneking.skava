/*
 * @(#)Resp.java		Created at 2018/9/7
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * https://github.com/ShaneKing/sk-js/blob/mirror/src/Resp.js
 */
@Accessors(chain = true)
@ToString(callSuper = true, includeFieldNames = true)
public class Resp<D> {
  @Getter
  @Setter
  private D data;//Business Data

  @Getter
  @Setter
  private boolean done;//true: No Unknown Exception,false: has Unknown Exception

  @Getter
  @Setter
  private RespMesg mesg;//Result Message Object, Required if done is false

  public static <D> Resp<D> build(D data, boolean done, RespMesg mesg) {
    return new Resp<D>().setData(data).setDone(done).setMesg(mesg);
  }

  public static <D> Resp<D> success(D data, RespMesg mesg) {
    return build(data, true, mesg);
  }

  public static <D> Resp<D> success(D data) {
    return success(data, null);
  }

  public static <D> Resp<D> failed(RespMesg mesg, D data) {
    return build(data, false, mesg);
  }

  public static <D> Resp<D> failed(RespMesg mesg) {
    return failed(mesg, null);
  }
}
