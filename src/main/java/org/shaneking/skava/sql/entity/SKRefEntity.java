/*
 * @(#)SKRefEntity.java		Created at 2017/9/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.shaneking.skava.sql.annotation.SKColumn;

@Accessors(chain = true)
@ToString(callSuper = true, includeFieldNames = true)
public class SKRefEntity<E> extends SKEntity<E> {
  @Getter
  @Setter
  @SKColumn(length = 40)
  private String refType;

  @Getter
  @Setter
  @SKColumn(length = 40)
  private String refId;
}
