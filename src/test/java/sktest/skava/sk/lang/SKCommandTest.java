package sktest.skava.sk.lang;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.sk.lang.SKCommand;
import sktest.skava.ling.io.File0Test;

public class SKCommandTest {
  @Test
  public void exec() {
    Assert.assertTrue(SKCommand.exec("ls " + File0Test.TEST_FILE.getAbsolutePath()));
  }
}
