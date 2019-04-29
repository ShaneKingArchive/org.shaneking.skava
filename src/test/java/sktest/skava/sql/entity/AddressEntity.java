/*
 * @(#)AddressEntity.java		Created at 2018/2/4
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sktest.skava.sql.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.shaneking.skava.sql.entity.SKRefEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Accessors(chain = true)
@ToString(callSuper = true, includeFieldNames = true)
@Table(name = "t_address", schema = "testSchema")
public class AddressEntity extends SKRefEntity {
  /**
   * InnoDB prefix index max 767 bytes(utf8:767/3=255char;gbk:767/2=383char)
   */
  @Getter
  @Setter
  @Column(length = 255)
  private String address;

  @Getter
  @Setter
  @Column(length = 6)
  private String postcode;

  @Getter
  @Setter
  @Column(length = 1)
  private String primary;//0|1\
}
