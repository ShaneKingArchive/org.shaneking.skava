package sktest.skava.rr;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.rr.RespMesg;
import sktest.skava.SKUnit;

public class RespMesgTest extends SKUnit {
  @Test
  public void build() {
    skPrint(RespMesg.build(RespMesg.INFO, "code", null));
  }

  @Test
  public void failed() {
    skPrint(RespMesg.failed("code"));
  }

  @Test
  public void failed1() {
    skPrint(RespMesg.failed("code", null));
  }

  @Test
  public void success() {
    skPrint(RespMesg.success("code"));
  }

  @Test
  public void success1() {
    skPrint(RespMesg.success("code", null));
  }

  @Test
  public void successfully() {
    Assert.assertEquals(true, RespMesg.success("code").successfully());
    Assert.assertEquals(false, RespMesg.failed("code").successfully());
  }

}
