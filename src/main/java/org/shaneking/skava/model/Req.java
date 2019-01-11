/*
 * @(#)SKModel.java		Created at 2018/9/7
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@ToString(includeFieldNames = true)
public class Req<E, O> {

  //maybe fastjson,gson,jackson...
  @Getter
  @Setter
  private E ext; // The E maybe JSONObject/JsonNode of request info contain pagination...

  @Getter
  @Setter
  private O obj;

  @Getter
  @Setter
  private E res;//just response, need exclude in request

  public static <E, O> Req<E, O> build() {
    return new Req<E, O>();
  }

  public static <E, O> Req<E, O> build(O obj) {
    return new Req<E, O>().setObj(obj);
  }

  public static <E, O> Req<E, O> build(O obj, E ext) {
    return new Req<E, O>().setObj(obj).setExt(ext);
  }
}
