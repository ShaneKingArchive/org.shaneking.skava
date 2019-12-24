package sktest.skava.lang;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.lang.Runtime0;

public class Runtime0Test {
  @Test
  public void exec() {
    Assert.assertTrue(Runtime0.exec("echo 123"));
    Assert.assertTrue(Runtime0.exec("echo 123", true, Runtime0.PAUSE_FLAG_SHELL));
    Assert.assertTrue(Runtime0.exec("echo 123", false, Runtime0.PAUSE_FLAG_SHELL));
    Assert.assertFalse(Runtime0.exec("skUnknownCommand"));
    Assert.assertFalse(Runtime0.exec("skUnknownCommand", true, Runtime0.PAUSE_FLAG_SHELL));
    Assert.assertFalse(Runtime0.exec("skUnknownCommand", false, Runtime0.PAUSE_FLAG_SHELL));
  }
}
