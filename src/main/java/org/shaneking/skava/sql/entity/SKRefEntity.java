/*
 * @(#)SKRefEntity.java		Created at 2017/9/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.entity;

import org.shaneking.skava.sql.annotation.SKColumn;

public class SKRefEntity extends SKEntity
{
  public static final String TYPE_OF_BRANCH = "branch";
  public static final String TYPE_OF_ORG    = "org";
  public static final String TYPE_OF_USER   = "user";

  @SKColumn(length = 36)
  private String refType;
  @SKColumn(length = 36)
  private String refUid;

  public String getRefType()
  {
    return refType;
  }

  public SKRefEntity setRefType(String refType)
  {
    this.refType = refType;
    return this;
  }

  public String getRefUid()
  {
    return refUid;
  }

  public SKRefEntity setRefUid(String refUid)
  {
    this.refUid = refUid;
    return this;
  }
}
