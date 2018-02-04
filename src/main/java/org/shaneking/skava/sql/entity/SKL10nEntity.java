/*
 * @(#)SKL10nEntity.java		Created at 2017/9/17
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.entity;

import org.shaneking.skava.sql.annotation.SKColumn;

public class SKL10nEntity extends SKEntity
{
  /**
   * @see org.shaneking.skava.ling.util.Date0#H_MI_S
   */
  @SKColumn(length = 6)
  private String insertTimeZone;
  /**
   * @see org.shaneking.skava.ling.util.Date0#H_MI_S
   */
  @SKColumn(length = 6)
  private String lastModifyTimeZone;
  /**
   * @see org.shaneking.skava.ling.util.Date0#H_MI_S
   */
  @SKColumn(length = 6)
  private String invalidTimeZone;

  public String getInsertTimeZone()
  {
    return insertTimeZone;
  }

  public SKL10nEntity setInsertTimeZone(String insertTimeZone)
  {
    this.insertTimeZone = insertTimeZone;
    return this;
  }

  public String getLastModifyTimeZone()
  {
    return lastModifyTimeZone;
  }

  public SKL10nEntity setLastModifyTimeZone(String lastModifyTimeZone)
  {
    this.lastModifyTimeZone = lastModifyTimeZone;
    return this;
  }

  public String getInvalidTimeZone()
  {
    return invalidTimeZone;
  }

  public SKL10nEntity setInvalidTimeZone(String invalidTimeZone)
  {
    this.invalidTimeZone = invalidTimeZone;
    return this;
  }
}
