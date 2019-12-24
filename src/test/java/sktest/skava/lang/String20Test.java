package sktest.skava.lang;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.lang.String20;
import sktest.skava.SKUnit;

public class String20Test extends SKUnit {

  @Test
  public void newInstance() {
    Assert.assertNotNull(String.valueOf(new String20()));
  }
}
