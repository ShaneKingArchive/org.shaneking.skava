package sktest.skava.rr;

import org.junit.Test;
import org.shaneking.skava.rr.Pri;
import org.shaneking.skava.rr.Req;
import sktest.skava.SKUnit;

public class ReqTest extends SKUnit {
  @Test
  public void build() {
    skPrint(Req.build().setEnc(""));
  }

  @Test
  public void buildPri() {
    skPrint(Req.build(Pri.build(new Object())));
  }

  @Test
  public void buildPub() {
    skPrint(Req.build(new Object()));
  }

}
