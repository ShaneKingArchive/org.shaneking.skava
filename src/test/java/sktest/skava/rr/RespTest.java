package sktest.skava.rr;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.rr.Resp;
import org.shaneking.skava.rr.RespException;

public class RespTest {
  @Test
  public void build1() {
    Assert.assertEquals("Resp(code=-1, data=data, mesg=mesg)", Resp.build(Resp.CODE_UNKNOWN_EXCEPTION, "data", "mesg").toString());
  }

  @Test
  public void build0() {
    Assert.assertEquals("Resp(code=0, data=data, mesg=mesg)", Resp.build(Resp.CODE_SUCCESSFULLY, "data", "mesg").toString());
  }

  @Test
  public void failedA0() {
    Assert.assertEquals("Resp(code=-1, data=null, mesg=null)", Resp.failed().toString());
  }

  @Test
  public void failedA1() {
//    Assert.assertEquals("{\"code\":\"1\",\"mesg\":\"NULL\"}", OM3.writeValueAsString(Resp.failed(Resp.CODE_KNOWN_EXCEPTION)));
    Assert.assertEquals("Resp(code=-1, data=null, mesg=null)", Resp.failed(Resp.CODE_UNKNOWN_EXCEPTION).toString());
  }

  @Test
  public void failedA2() {
    Assert.assertEquals("Resp(code=-1, data=null, mesg=mesg)", Resp.failed(Resp.CODE_UNKNOWN_EXCEPTION, "mesg").toString());
  }

  @Test
  public void failedA3() {
    Assert.assertEquals("Resp(code=-1, data=data, mesg=mesg)", Resp.failed(Resp.CODE_UNKNOWN_EXCEPTION, "mesg", "data").toString());
  }

  @Test
  public void parseExp() {
    Assert.assertEquals("Resp(code=code, data=null, mesg=mesg)", Resp.<Integer>failed().parseExp(new RespException(Resp.failed("code", "mesg", "data"))).toString());
    Assert.assertEquals("Resp(code=code, data=null, mesg=mesg)", Resp.<Integer>failed().parseExp(new RespException(Resp.failed("code", "mesg", "data"))).toString());
  }

  @Test
  public void success() {
    Assert.assertEquals("Resp(code=0, data=data, mesg=null)", Resp.success("data").toString());
  }
}
