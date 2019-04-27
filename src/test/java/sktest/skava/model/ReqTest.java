package sktest.skava.model;

import org.junit.Test;
import org.shaneking.skava.model.Pri;
import org.shaneking.skava.model.Req;
import org.shaneking.skava.model.Resp;
import org.shaneking.skava.model.RespMesg;
import sktest.skava.SKUnit;

public class ReqTest extends SKUnit {
  @Test
  public void testToString() throws Exception {
    skPrint(Req.build().setPub(Resp.success("data")).setPri(Pri.build(RespMesg.success("code"))));
  }

}
