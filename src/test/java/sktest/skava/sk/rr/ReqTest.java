package sktest.skava.sk.rr;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.sk.rr.Pri;
import org.shaneking.skava.sk.rr.Req;
import org.shaneking.skava.t3.jackson.OM3;
import sktest.skava.SKUnit;

public class ReqTest extends SKUnit {
  @Test
  public void build() {
    Assert.assertEquals("{}", OM3.writeValueAsString(Req.build().setEnc("")));
  }

  @Test
  public void buildPri() {
    Assert.assertEquals("{\"pri\":{}}", OM3.writeValueAsString(Req.build(Pri.build(new Object()))));
  }

  @Test
  public void buildPub() {
    Assert.assertEquals("{}", OM3.writeValueAsString(Req.build(new Object())));
  }

}
