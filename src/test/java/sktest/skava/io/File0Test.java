package sktest.skava.io;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.io.File0;
import org.shaneking.skava.lang.String0;

import java.io.File;
import java.nio.file.Paths;

public class File0Test {
  public static final String TEST_FOLDER = "src/test";
  public static final String TEST_TXT = "resources/sktest.skava.ling.io.File0Test.txt";
  public static final File TEST_FILE = new File(TEST_FOLDER + String0.SLASH + TEST_TXT);

  @Test
  public void join() {
    System.out.println(TEST_FILE.getAbsolutePath());
    Assert.assertEquals(Paths.get(TEST_FOLDER, TEST_TXT).toFile().getAbsolutePath(), File0.join(new File(TEST_FOLDER), TEST_TXT).getAbsolutePath());
  }

  @Test
  public void join1() {
    Assert.assertEquals(Paths.get(TEST_FOLDER, TEST_TXT).toFile().getAbsolutePath(), File0.join(String0.SLASH, TEST_FOLDER, TEST_TXT).getAbsolutePath());
  }

  @Test
  public void join2() {
    Assert.assertEquals(Paths.get(TEST_FOLDER, TEST_TXT).toFile().getAbsolutePath(), File0.join(String0.SLASH, new File(TEST_FOLDER), TEST_TXT).getAbsolutePath());
  }
}
