package sktest.skava.lang;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.lang.Object0;
import org.shaneking.skava.lang.String0;
import org.shaneking.skava.rr.Req;
import org.shaneking.skava.rr.Resp;

public class Object0Test {
  public static final String TEST_STRING = "str";

  @Test
  public void g() {
    Assert.assertEquals(TEST_STRING, Object0.gs(Resp.success(Req.build().setEnc(TEST_STRING)), "data.enc"));
    Assert.assertNull(Object0.gs(Resp.success(Req.build().setEnc(TEST_STRING)), "data.enc.str"));
    Assert.assertNull(Object0.gs(Resp.success(Req.build().setEnc(TEST_STRING)), "data.str"));
    Assert.assertEquals(String0.EMPTY, String0.valueOf(Object0.gs(Resp.success(Req.build().setEnc(TEST_STRING)), "data.enc.str")));
    Assert.assertEquals(String0.EMPTY, String0.valueOf(Object0.gs(Resp.success(Req.build().setEnc(TEST_STRING)), "data.str")));
  }

  @Test
  public void s() {
    Resp resp = Resp.success(Req.build());
    Object0.gs(resp, "data.enc", TEST_STRING);
    Assert.assertEquals("Resp(code=0, data=Req(enc=str, pri=null, pub=null), mesg=null)", resp.toString());
    Assert.assertNull(Object0.gs(Resp.success(Req.build().setEnc(TEST_STRING)), "data.enc.str", "123"));
    Assert.assertNull(Object0.gs(Resp.success(Req.build().setEnc(TEST_STRING)), "data.str", "123"));
  }
}
