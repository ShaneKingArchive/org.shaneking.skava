package sktest.skava.security;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.security.Key0;
import sktest.skava.SKUnit;

public class Key0Test extends SKUnit {

  @Test
  public void newInstance() {
    Assert.assertNotNull(String.valueOf(new Key0()));
  }
}
