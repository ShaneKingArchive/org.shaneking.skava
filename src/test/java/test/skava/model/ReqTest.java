package test.skava.model;

import org.junit.Test;
import org.shaneking.skava.model.Req;
import org.shaneking.skava.model.Resp;
import org.shaneking.skava.model.RespMesg;
import test.skava.SKUnit;

public class ReqTest extends SKUnit {
  @Test
  public void testToString() throws Exception {
    skPrint(Req.build().setObj(Resp.success("data")).setExt(RespMesg.success("code")));
  }

}
