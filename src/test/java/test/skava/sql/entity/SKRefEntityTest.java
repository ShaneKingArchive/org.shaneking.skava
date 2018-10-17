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

public class SKRefEntityTest extends SKUnit {
  @Test
  public void testQueryListSql() {
    AddressEntity addressEntity = new AddressEntity();
    Assert.assertEquals(addressEntity.insertSql().toString(), "(insert into testSchema.t_address (version) values (?),[1])");
    Assert.assertEquals(addressEntity.selectSql().toString(), "(select address,postcode,primary,ref_type,ref_id,id,version,ext_json,create_datetime,create_user_id,last_modify_datetime,last_modify_user_id,invalid,invalid_datetime,invalid_user_id from testSchema.t_address where version=?,[1])");
    String uuid = UUID.randomUUID().toString();
    addressEntity.setAddress("Junjin Load No.500").setId(uuid);
    Assert.assertEquals(addressEntity.insertSql().toString(), "(insert into testSchema.t_address (address,id,version) values (?,?,?),[Junjin Load No.500, " + uuid + ", 1])");
    Assert.assertEquals(addressEntity.updateByIdAndVersionSql().toString(), "(update testSchema.t_address set address=?,version=? where id=? and version=?,[Junjin Load No.500, 2, " + uuid + ", 1])");
    Assert.assertEquals(addressEntity.selectSql().toString(), "(select address,postcode,primary,ref_type,ref_id,id,version,ext_json,create_datetime,create_user_id,last_modify_datetime,last_modify_user_id,invalid,invalid_datetime,invalid_user_id from testSchema.t_address where address=? and id=? and version=?,[Junjin Load No.500, " + uuid + ", 1])");
  }
}



