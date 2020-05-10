package sktest.skava.security.spec;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.security.spec.KeySpec0;

public class KeySpec0Test {
  @Test
  public void newInstance() {
    Assert.assertNotNull(String.valueOf(new KeySpec0()));
  }
}
