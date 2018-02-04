/*
 * @(#)AddressEntity.java		Created at 2018/2/4
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package test.skava.sql.entity;

import org.shaneking.skava.sql.annotation.SKColumn;
import org.shaneking.skava.sql.annotation.SKTable;
import org.shaneking.skava.sql.entity.SKRefEntity;

@SKTable(name = "t_address", schema = "testSchema")
public class AddressEntity extends SKRefEntity
{
  private String address;
  @SKColumn(length = 6)
  private String postcode;
  @SKColumn(length = 1)
  private String primary;//0|1

  public String getAddress()
  {
    return address;
  }

  public AddressEntity setAddress(String address)
  {
    this.address = address;
    return this;
  }

  public String getPostcode()
  {
    return postcode;
  }

  public AddressEntity setPostcode(String postcode)
  {
    this.postcode = postcode;
    return this;
  }

  public String getPrimary()
  {
    return primary;
  }

  public AddressEntity setPrimary(String primary)
  {
    this.primary = primary;
    return this;
  }
}
