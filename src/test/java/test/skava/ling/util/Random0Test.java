package test.skava.ling.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.ling.util.Random0;

public class Random0Test
{
  @Before
  public void setUp() throws Exception
  {
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public void nextMaxInt() throws Exception
  {
    Assert.assertNotEquals(10, Random0.nextMaxInt(10));
  }

}
