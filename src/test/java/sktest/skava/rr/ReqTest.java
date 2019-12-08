package sktest.skava.rr;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.rr.Pri;
import org.shaneking.skava.rr.Req;
import sktest.skava.SKUnit;

public class ReqTest extends SKUnit {
  @Test
  public void build() {
    Assert.assertEquals("Req(enc=enc, pri=null, pub=null)", Req.build().setEnc("enc").toString());
  }

  @Test
  public void buildPri() {
    Assert.assertEquals("Req(enc=null, pri=Pri(ext=null, obj=null, rtn=rtn), pub=null)", Req.build(Pri.build("rtn")).toString());
  }

  @Test
  public void buildPub() {
    Assert.assertEquals("Req(enc=null, pri=null, pub=pub)", Req.build("pub").toString());
  }

}
