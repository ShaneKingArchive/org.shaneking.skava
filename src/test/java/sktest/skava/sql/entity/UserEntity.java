/*
 * @(#)UserEntity.java		Created at 2018/2/4
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sktest.skava.sql.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.shaneking.skava.sql.annotation.SKColumn;
import org.shaneking.skava.sql.annotation.SKTable;
import org.shaneking.skava.sql.entity.SKEntity;

@Accessors(chain = true)
@ToString(callSuper = true, includeFieldNames = true)
@SKTable
public class UserEntity extends SKEntity {
  @Getter
  @Setter
  private String name;

  /**
   * @see org.shaneking.skava.ling.util.Date0#Y_M_D
   */
  @Getter
  @Setter
  @SKColumn(length = 10)
  private String birthday;
}
