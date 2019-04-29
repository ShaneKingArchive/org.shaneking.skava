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
import org.shaneking.skava.sql.entity.SKEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Accessors(chain = true)
@ToString(callSuper = true, includeFieldNames = true)
@Table
public class UserEntity extends SKEntity {
  @Getter
  @Setter
  private String name;

  /**
   * @see org.shaneking.skava.ling.util.Date0#Y_M_D
   */
  @Getter
  @Setter
  @Column(length = 10)
  private String birthday;
}
