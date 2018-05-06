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
  private String createTimezone;
  /**
   * @see org.shaneking.skava.ling.util.Date0#H_MI_S
   */
  @SKColumn(length = 6)
  private String lastModifyTimezone;
  /**
   * @see org.shaneking.skava.ling.util.Date0#H_MI_S
   */
  @SKColumn(length = 6)
  private String invalidTimezone;

  public String getCreateTimezone()
  {
    return createTimezone;
  }

  public SKL10nEntity setCreateTimezone(String createTimezone)
  {
    this.createTimezone = createTimezone;
    return this;
  }

  public String getLastModifyTimezone()
  {
    return lastModifyTimezone;
  }

  public SKL10nEntity setLastModifyTimezone(String lastModifyTimezone)
  {
    this.lastModifyTimezone = lastModifyTimezone;
    return this;
  }

  public String getInvalidTimezone()
  {
    return invalidTimezone;
  }

  public SKL10nEntity setInvalidTimezone(String invalidTimezone)
  {
    this.invalidTimezone = invalidTimezone;
    return this;
  }
}
