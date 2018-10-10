package test.skava.model;

import org.junit.Test;
import org.shaneking.skava.model.SKModel;
import org.shaneking.skava.resp.Resp;
import org.shaneking.skava.resp.RespMesg;
import test.skava.SKUnit;

public class SKModelTest extends SKUnit {
  @Test
  public void testToString() throws Exception {
    skPrint(new SKModel<Resp, RespMesg>().setObj(Resp.success("data")).setUi(RespMesg.success("code")));
  }

}
