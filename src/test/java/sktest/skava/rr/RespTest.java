package sktest.skava.rr;

import org.junit.Test;
import org.shaneking.skava.rr.Resp;
import org.shaneking.skava.rr.RespMesg;
import sktest.skava.SKUnit;

public class RespTest extends SKUnit {

  @Test
  public void build() {
    skPrint(Resp.build("data", true, RespMesg.build(RespMesg.INFO, "code", null)));
  }

  @Test
  public void failed() {
    skPrint(Resp.failed(RespMesg.failed("code")));
  }

  @Test
  public void failed1() {
    skPrint(Resp.failed(RespMesg.failed("code"), "data"));
  }

  @Test
  public void success() {
    skPrint(Resp.success("data"));
  }

  @Test
  public void success1() {
    skPrint(Resp.success("data", RespMesg.success("code")));
  }

}
