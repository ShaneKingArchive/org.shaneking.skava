package sktest.skava.rr;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.rr.Pri;
import sktest.skava.SKUnit;

public class PriTest extends SKUnit {
  @Test
  public void build() {
    Assert.assertEquals("Pri(ext=ext, obj=obj, rtn=rtn)", Pri.build().setExt("ext").setObj("obj").setRtn("rtn").toString());
  }

  @Test
  public void buildRtn() {
    Assert.assertEquals("Pri(ext=null, obj=null, rtn=rtn)", Pri.build("rtn").toString());
  }
}
