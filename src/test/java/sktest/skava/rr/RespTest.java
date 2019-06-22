package sktest.skava.rr;

import org.junit.Test;
import org.shaneking.skava.rr.Resp;
import sktest.skava.SKUnit;

public class RespTest extends SKUnit {

  @Test
  public void build() {
    skPrint(Resp.build(Resp.CODE_UNKNOWN_EXCEPTION, "data", "mesg"));
  }

  @Test
  public void buildSuccessfully() {
    skPrint(Resp.build(Resp.CODE_SUCCESSFULLY, "data", "mesg"));
  }

  @Test
  public void failed_a0() {
    skPrint(Resp.failed());
  }

  @Test
  public void failed_a1() {
    skPrint(Resp.failed("mesg"));
  }

  @Test
  public void failed_a2() {
    skPrint(Resp.failed("mesg", Resp.CODE_KNOWN_EXCEPTION));
  }

  @Test
  public void failed_a3() {
    skPrint(Resp.failed("mesg", Resp.CODE_KNOWN_EXCEPTION, "data"));
  }

  @Test
  public void success() {
    skPrint(Resp.success("data"));
  }

}
