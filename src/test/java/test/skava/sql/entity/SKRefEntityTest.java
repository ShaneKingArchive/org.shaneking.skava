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
    Assert.assertEquals(addressEntity.selectSql().toString(), "(select uid,version,ext_json,create_datetime,create_user_uid,last_modify_datetime,last_modify_user_uid,invalid,invalid_datetime,invalid_user_uid,ref_type,ref_uid,address,postcode,primary from testSchema.t_address where version=?,[1])");
    String uuid = UUID.randomUUID().toString();
    addressEntity.setAddress("Junjin Load No.500").setUid(uuid);
    Assert.assertEquals(addressEntity.insertSql().toString(), "(insert into testSchema.t_address (uid,version,address) values (?,?,?),[" + uuid + ", 1, Junjin Load No.500])");
    Assert.assertEquals(addressEntity.updateByUidAndVersionSql().toString(), "(update testSchema.t_address set version=?,address=? where uid=? and version=?,[2, Junjin Load No.500, " + uuid + ", 1])");
    Assert.assertEquals(addressEntity.selectSql().toString(), "(select uid,version,ext_json,create_datetime,create_user_uid,last_modify_datetime,last_modify_user_uid,invalid,invalid_datetime,invalid_user_uid,ref_type,ref_uid,address,postcode,primary from testSchema.t_address where uid=? and version=? and address=?,[" + uuid + ", 1, Junjin Load No.500])");
  }
}



