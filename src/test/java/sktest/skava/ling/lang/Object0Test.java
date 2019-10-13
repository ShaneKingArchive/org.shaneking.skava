package sktest.skava.ling.lang;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.lang.Object0;
import org.shaneking.skava.ling.lang.String0;
import org.shaneking.skava.sk.rr.Req;
import org.shaneking.skava.sk.rr.Resp;
import org.shaneking.skava.t3.jackson.OM3;

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
    Assert.assertEquals("{\"code\":\"0\",\"data\":{\"enc\":\"str\"}}", OM3.writeValueAsString(resp));
    Assert.assertNull(Object0.gs(Resp.success(Req.build().setEnc(TEST_STRING)), "data.enc.str", "123"));
    Assert.assertNull(Object0.gs(Resp.success(Req.build().setEnc(TEST_STRING)), "data.str", "123"));
  }
}
