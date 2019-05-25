package sktest.skava.rr;

import org.junit.Test;
import org.shaneking.skava.rr.Resp;
import sktest.skava.SKUnit;

public class RespTest extends SKUnit {

  @Test
  public void build() {
    skPrint(Resp.build("data", true, "code", Resp.INFO));
  }

  @Test
  public void failed() {
    skPrint(Resp.failed("code", Resp.INFO));
  }

  @Test
  public void failed1() {
    skPrint(Resp.failed("code", Resp.INFO, "data"));
  }

  @Test
  public void success() {
    skPrint(Resp.success("data"));
  }

}
