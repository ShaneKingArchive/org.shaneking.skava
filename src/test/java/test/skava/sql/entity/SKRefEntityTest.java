/*
 * @(#)AddressEntityTest.java		Created at 2017/9/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package test.skava.sql.entity;

import org.junit.Test;
import test.skava.SKUnit;

import java.util.UUID;

public class SKRefEntityTest extends SKUnit
{
  @Test
  public void testQueryListSql()
  {
    AddressEntity addressEntity = new AddressEntity();
    skPrint(addressEntity.insertSql().toString());
    skPrint(addressEntity.selectSql().toString());
    addressEntity.setId(UUID.randomUUID().toString());
    skPrint(addressEntity.insertSql().toString());
    skPrint(addressEntity.updateByIdAndVersionSql().toString());
    skPrint(addressEntity.selectSql().toString());
  }
}



