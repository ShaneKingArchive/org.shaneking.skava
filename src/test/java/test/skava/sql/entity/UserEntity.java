/*
 * @(#)UserEntity.java		Created at 2018/2/4
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package test.skava.sql.entity;

import org.shaneking.skava.sql.annotation.SKColumn;
import org.shaneking.skava.sql.annotation.SKTable;
import org.shaneking.skava.sql.entity.SKEntity;

@SKTable
public class UserEntity extends SKEntity
{
  private String name;
  /**
   * @see org.shaneking.skava.ling.util.Date0#Y_M_D
   */
  @SKColumn(length = 10)
  private String birthday;

  public String getName()
  {
    return name;
  }

  public UserEntity setName(String name)
  {
    this.name = name;
    return this;
  }

  public String getBirthday()
  {
    return birthday;
  }

  public UserEntity setBirthday(String birthday)
  {
    this.birthday = birthday;
    return this;
  }
}
