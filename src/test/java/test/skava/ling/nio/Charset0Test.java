package test.skava.ling.nio;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.nio.Charset0;
import test.skava.SKUnit;

public class Charset0Test extends SKUnit {
  @Test
  public void staticProperties() {
    Assert.assertEquals(Charset0.UTF_8, "UTF-8");
  }
}
