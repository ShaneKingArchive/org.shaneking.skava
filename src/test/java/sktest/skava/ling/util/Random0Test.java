package sktest.skava.ling.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.ling.util.Random0;

public class Random0Test {
  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void newInstance() {
    Assert.assertNotNull(new Random0().toString());
  }

  @Test
  public void nextMaxInt() {
    Assert.assertNotEquals(10, Random0.nextMaxInt(10));
  }

}
