package sktest.skava.crypto;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.crypto.Cipher0;
import sktest.skava.SKUnit;

public class Cipher0Test extends SKUnit {

  @Test
  public void newInstance() {
    Assert.assertNotNull(String.valueOf(new Cipher0()));
  }
}
