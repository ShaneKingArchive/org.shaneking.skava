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
public class Pri<J, O> {
  @Getter
  @Setter
  private J rtn;//return, just response, don't request

  @Getter
  @Setter
  private J ext;//extend settings like table config

  @Getter
  @Setter
  private O obj;//main object content

  public static <J, O> Pri<J, O> build() {
    return new Pri<J, O>();
  }

  public static <J, O> Pri<J, O> build(J rtn) {
    return new Pri<J, O>().setRtn(rtn);
  }

  public static <J, O> Pri<J, O> build(J ext, O obj) {
    return new Pri<J, O>().setExt(ext).setObj(obj);
  }
}
