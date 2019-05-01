package sktest.skava.sql.entity;

import org.junit.Assert;
import org.junit.Test;
import sktest.skava.SKUnit;
import sktest.skava.sql.entity.prepare.PrepareSKL10nEntityZone;

public class SKL10nEntityTest extends SKUnit {
  @Test
  public void testInsertSql() {
    PrepareSKL10nEntityZone prepareSKL10NEntityZone = new PrepareSKL10nEntityZone();
    prepareSKL10NEntityZone.setCreateTimezone("").setInvalidTimezone("").setLastModifyTimezone("");
    skPrint(prepareSKL10NEntityZone);
    Assert.assertEquals(prepareSKL10NEntityZone.insertSql().toString(), "(insert into t_prepare_s_k_l_1_0n_entity_zone (version) values (?),[1])");
  }
}
