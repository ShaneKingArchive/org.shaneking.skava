package sktest.skava.model;

import org.junit.Test;
import org.shaneking.skava.rr.Pri;
import org.shaneking.skava.rr.Req;
import org.shaneking.skava.rr.Resp;
import org.shaneking.skava.rr.RespMesg;
import sktest.skava.SKUnit;

public class ReqTest extends SKUnit {
  @Test
  public void testToString() throws Exception {
    skPrint(Req.build().setPub(Resp.success("data")).setPri(Pri.build(RespMesg.success("code"))));
  }

}
