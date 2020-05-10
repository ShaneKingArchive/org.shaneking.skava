package sktest.skava.crypto;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.crypto.Cipher0;

public class Cipher0Test {
  @Test
  public void newInstance() {
    Assert.assertNotNull(String.valueOf(new Cipher0()));
  }
}
