package sktest.skava.sk.lang;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.sk.lang.SKCommand;

public class SKCommandTest {
  @Test
  public void exec() {
    Assert.assertTrue(SKCommand.exec("echo 123"));
    Assert.assertFalse(SKCommand.exec("skUnknownCommand"));
  }
}
