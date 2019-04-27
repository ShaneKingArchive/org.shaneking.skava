package sktest.skava.model;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.model.RespMesg;
import sktest.skava.SKUnit;

public class RespMesgTest extends SKUnit {
  @Test
  public void build() throws Exception {
    skPrint(RespMesg.build(RespMesg.INFO, "code", null));
  }

  @Test
  public void success() throws Exception {
    skPrint(RespMesg.success("code"));
  }

  @Test
  public void success1() throws Exception {
    skPrint(RespMesg.success("code", null));
  }

  @Test
  public void failed() throws Exception {
    skPrint(RespMesg.failed("code"));
  }

  @Test
  public void failed1() throws Exception {
    skPrint(RespMesg.failed("code", null));
  }

  @Test
  public void successfully() throws Exception {
    Assert.assertEquals(true, RespMesg.success("code").successfully());
    Assert.assertEquals(false, RespMesg.failed("code").successfully());
  }

}
