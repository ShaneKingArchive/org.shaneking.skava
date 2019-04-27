package sktest.skava.sql.entity;

import org.junit.Assert;
import org.junit.Test;
import sktest.skava.SKUnit;

public class SKL10nEntityTest extends SKUnit {
  @Test
  public void test() {
    ZoneEntity zoneEntity = new ZoneEntity();
    Assert.assertEquals(zoneEntity.insertSql().toString(), "(insert into t_zone_entity (version) values (?),[1])");
  }
}
