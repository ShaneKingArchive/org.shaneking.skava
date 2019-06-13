/*
 * @(#)Pri.java		Created at 2019/1/11
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.rr;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

//Pri is trans object wrapper
@Accessors(chain = true)
@ToString
public class Pri<E, O, R> {

  @Getter
  @Setter
  private E ext;//extend settings like table config

  @Getter
  @Setter
  private O obj;//main object content

  @Getter
  @Setter
  private R rtn;//return, just response, don't request

  public static <E, O, R> Pri<E, O, R> build() {
    return new Pri<E, O, R>();
  }

  public static <E, O, R> Pri<E, O, R> build(R rtn) {
    return new Pri<E, O, R>().setRtn(rtn);
  }

  public static <E, O, R> Pri<E, O, R> build(E ext, O obj) {
    return new Pri<E, O, R>().setExt(ext).setObj(obj);
  }
}
