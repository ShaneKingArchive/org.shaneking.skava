/*
 * @(#)AddressEntityTest.java		Created at 2017/9/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package test.skava.sql.entity;

import org.junit.Assert;
import org.junit.Test;
import test.skava.SKUnit;

import java.util.UUID;

public class SKRefEntityTest extends SKUnit
{
  @Test
  public void testQueryListSql()
  {
    AddressEntity addressEntity = new AddressEntity();
    Assert.assertEquals(addressEntity.insertSql().toString(), "(insert into testSchema.t_address (version) values (?),[1])");
    Assert.assertEquals(addressEntity.selectSql().toString(), "(select id,version,ext_json,create_datetime,create_user_id,last_modify_datetime,last_modify_user_id,invalid,invalid_datetime,invalid_user_id,ref_type,ref_uid,postcode,primary from testSchema.t_address where version=?,[1])");
    String uuid = UUID.randomUUID().toString();
    addressEntity.setId(uuid);
    Assert.assertEquals(addressEntity.insertSql().toString(), "(insert into testSchema.t_address (id,version) values (?,?),[" + uuid + ", 1])");
    Assert.assertEquals(addressEntity.updateByIdAndVersionSql().toString(), "(update testSchema.t_address set version=? where id=? and version=?,[2, " + uuid + ", 1])");
    Assert.assertEquals(addressEntity.selectSql().toString(), "(select id,version,ext_json,create_datetime,create_user_id,last_modify_datetime,last_modify_user_id,invalid,invalid_datetime,invalid_user_id,ref_type,ref_uid,postcode,primary from testSchema.t_address where id=? and version=?,[" + uuid + ", 1])");
  }
}



