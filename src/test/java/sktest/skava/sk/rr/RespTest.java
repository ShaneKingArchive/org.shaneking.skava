package sktest.skava.sk.rr;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.sk.rr.Resp;
import org.shaneking.skava.t3.jackson.OM3;
import sktest.skava.SKUnit;

public class RespTest extends SKUnit {

  @Test
  public void build() {
    Assert.assertEquals("{\"code\":\"-1\",\"data\":\"data\",\"mesg\":\"mesg\"}", OM3.writeValueAsString(Resp.build(Resp.CODE_UNKNOWN_EXCEPTION, "data", "mesg")));
  }

  @Test
  public void buildSuccessfully() {
    Assert.assertEquals("{\"code\":\"0\",\"data\":\"data\",\"mesg\":\"mesg\"}", OM3.writeValueAsString(Resp.build(Resp.CODE_SUCCESSFULLY, "data", "mesg")));
  }

  @Test
  public void failedA0() {
    Assert.assertEquals("{\"code\":\"-1\",\"mesg\":\"NULL\"}", OM3.writeValueAsString(Resp.failed()));
  }

  @Test
  public void failedA1() {
    Assert.assertEquals("{\"code\":\"1\",\"mesg\":\"NULL\"}", OM3.writeValueAsString(Resp.failed(Resp.CODE_KNOWN_EXCEPTION)));
    Assert.assertEquals("{\"code\":\"-1\",\"mesg\":\"NULL\"}", OM3.writeValueAsString(Resp.failed(Resp.CODE_UNKNOWN_EXCEPTION)));
  }

  @Test
  public void failedA2() {
    Assert.assertEquals("{\"code\":\"1\",\"mesg\":\"mesg\"}", OM3.writeValueAsString(Resp.failed(Resp.CODE_KNOWN_EXCEPTION, "mesg")));
  }

  @Test
  public void failedA3() {
    Assert.assertEquals("{\"code\":\"1\",\"data\":\"data\",\"mesg\":\"mesg\"}", OM3.writeValueAsString(Resp.failed(Resp.CODE_KNOWN_EXCEPTION, "mesg", "data")));
  }

  @Test
  public void success() {
    Assert.assertEquals("{\"code\":\"0\",\"data\":\"data\"}", OM3.writeValueAsString(Resp.success("data")));
  }

}
