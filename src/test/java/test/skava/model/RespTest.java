package test.skava.model;

import org.junit.Test;
import org.shaneking.skava.model.Resp;
import org.shaneking.skava.model.RespMesg;
import test.skava.SKUnit;

public class RespTest extends SKUnit {

  @Test
  public void build() throws Exception {
    skPrint(Resp.build("data", true, RespMesg.build(RespMesg.INFO, "code", null)));
  }

  @Test
  public void success() throws Exception {
    skPrint(Resp.success("data"));
  }

  @Test
  public void success1() throws Exception {
    skPrint(Resp.success("data", RespMesg.success("code")));
  }

  @Test
  public void failed() throws Exception {
    skPrint(Resp.failed(RespMesg.failed("code")));
  }

  @Test
  public void failed1() throws Exception {
    skPrint(Resp.failed(RespMesg.failed("code"), "data"));
  }

}
