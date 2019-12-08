package sktest.skava.ling.crypto;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.crypto.Cipher0;
import sktest.skava.SKUnit;

public class Cipher0Test extends SKUnit {

  @Test
  public void newInstance() {
    Assert.assertNotNull(String.valueOf(new Cipher0()));
  }
}
