/*
 * @(#)SKModel.java		Created at 2018/9/7
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.rr;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

//J maybe fastjson,gson,jackson...
@Accessors(chain = true)
@ToString
public class Req<J, O> {

  @Getter
  @Setter
  private String enc;//this is encode of pri

  @Getter
  @Setter
  private Pri<J, O> pri;//main object

  @Getter
  @Setter
  private J pub;//system properties;tenantId,appName,sysName,priEncode(if Y, parse enc to pri),priPath('res')

  public static <J, O> Req<J, O> build() {
    return new Req<J, O>();
  }

  public static <J, O> Req<J, O> build(Pri<J, O> pri) {
    return new Req<J, O>().setPri(pri);
  }

  public static <J, O> Req<J, O> build(J pub) {
    return new Req<J, O>().setPub(pub);
  }
}
