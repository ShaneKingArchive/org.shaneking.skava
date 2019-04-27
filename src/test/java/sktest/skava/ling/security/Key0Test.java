package sktest.skava.ling.security;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.security.Key0;
import sktest.skava.SKUnit;

public class Key0Test extends SKUnit {
  @Test
  public void staticProperties() {
    Assert.assertEquals(Key0.AES, "AES");
  }
}
