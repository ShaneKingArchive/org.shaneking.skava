/*
 * @(#)Resp.java		Created at 2018/9/7
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * https://github.com/ShaneKing/sk-js/blob/mirror/src/Resp.js
 */
@Accessors(chain = true)
@ToString(includeFieldNames = true)
public class Resp<D, M> {
  @Getter
  @Setter
  private D data;//Business Object

  @Getter
  @Setter
  private boolean done;//true: No Unknown Exception,false: has Unknown Exception

  @Getter
  @Setter
  private RespMesg<M> mesg;//Result Message Object, Required if done is false

  public static <D, M> Resp<D, M> build(D data, boolean done, RespMesg<M> mesg) {
    return new Resp<D, M>().setData(data).setDone(done).setMesg(mesg);
  }

  public static <D, M> Resp<D, M> success(D data, RespMesg<M> mesg) {
    return build(data, true, mesg);
  }

  public static <D, M> Resp<D, M> success(D data) {
    return success(data, null);
  }

  public static <D, M> Resp<D, M> failed(RespMesg<M> mesg, D data) {
    return build(data, false, mesg);
  }

  public static <D, M> Resp<D, M> failed(RespMesg<M> mesg) {
    return failed(mesg, null);
  }
}
