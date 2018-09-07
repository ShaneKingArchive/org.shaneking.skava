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
@ToString(callSuper = true, includeFieldNames = true)
public class SKModel<O, U>
{
  @Getter
  @Setter
  private O obj;

  @Getter
  @Setter
  private U ui; // The U maybe JSONObject, JsonNode ...
}
