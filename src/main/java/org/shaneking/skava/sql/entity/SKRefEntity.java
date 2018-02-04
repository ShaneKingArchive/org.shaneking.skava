/*
 * @(#)SKRefEntity.java		Created at 2017/9/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.entity;

import org.shaneking.skava.sql.annotation.SKColumn;
import org.shaneking.skava.sql.annotation.SKTransient;

public class SKRefEntity extends SKEntity
{
  @SKTransient
  public static final String TYPE_OF_USER = "user";

  private String refType;
  @SKColumn(length = 36)
  private String refId;

  public String getRefType()
  {
    return refType;
  }

  public SKRefEntity setRefType(String refType)
  {
    this.refType = refType;
    return this;
  }

  public String getRefId()
  {
    return refId;
  }

  public SKRefEntity setRefId(String refId)
  {
    this.refId = refId;
    return this;
  }
}
