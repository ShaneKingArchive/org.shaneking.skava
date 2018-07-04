package test.skava.ling.lang;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.lang.String0;
import test.skava.SKUnit;

public class String0Test extends SKUnit
{
  @Test
  public void upper2lower() throws Exception
  {
    Assert.assertEquals(String0.upper2lower("firstName"), "first_name");
  }

}
