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
public class Req<O, E> {
  @Getter
  @Setter
  private O obj;

  @Getter
  @Setter
  private E ext; // The E maybe JSONObject/JsonNode of request info contain pagination...
}
