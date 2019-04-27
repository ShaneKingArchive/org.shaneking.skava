package sktest.skava.ling.security.spec;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.security.spec.KeySpec0;
import sktest.skava.SKUnit;

public class KeySpec0Test extends SKUnit {
  @Test
  public void staticProperties() {
    Assert.assertEquals(KeySpec0.PBKDF2WithHmacSHA1, "PBKDF2WithHmacSHA1");
  }
}
