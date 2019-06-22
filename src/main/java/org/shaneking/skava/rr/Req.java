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

/**
 * Just reference for request and response, overwrite it in most scenarios
 * <p>
 * //E, O, R, P maybe fastjson,gson,jackson...
 */
@Accessors(chain = true)
@ToString
public class Req<E, O, R, P> {

  @Getter
  @Setter
  private String enc;//this is encode of pri

  @Getter
  @Setter
  private Pri<E, O, R> pri;//main object

  @Getter
  @Setter
  private P pub;//system properties;tenantId,appName,sysName,priEncode(if Y, parse enc to pri),priPath('res')

  public static <E, O, R, P> Req<E, O, R, P> build() {
    return new Req<E, O, R, P>();
  }

  public static <E, O, R, P> Req<E, O, R, P> build(Pri<E, O, R> pri) {
    return new Req<E, O, R, P>().setPri(pri);
  }

  public static <E, O, R, P> Req<E, O, R, P> build(P pub) {
    return new Req<E, O, R, P>().setPub(pub);
  }
}
