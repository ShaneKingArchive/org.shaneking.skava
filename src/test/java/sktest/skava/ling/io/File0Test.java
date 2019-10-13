package sktest.skava.ling.io;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.io.File0;
import org.shaneking.skava.ling.lang.String0;

import java.io.File;

public class File0Test {
  public static final String TEST_FOLDER = "src/test";
  public static final String TEST_TXT = "resources/sktest.skava.ling.io.File0Test.txt";
  public static final File TEST_FILE = new File(TEST_FOLDER + String0.SLASH + TEST_TXT);

  @Test
  public void join() {
    System.out.println(TEST_FILE.getAbsolutePath());
    Assert.assertEquals(TEST_FILE.getAbsolutePath(), File0.join(new File(TEST_FOLDER), TEST_TXT).getAbsolutePath());
  }

  @Test
  public void join1() {
    Assert.assertEquals(TEST_FILE.getAbsolutePath(), File0.join(String0.SLASH, TEST_FOLDER, TEST_TXT).getAbsolutePath());
  }

  @Test
  public void join2() {
    Assert.assertEquals(TEST_FILE.getAbsolutePath(), File0.join(String0.SLASH, new File(TEST_FOLDER), TEST_TXT).getAbsolutePath());
  }
}
