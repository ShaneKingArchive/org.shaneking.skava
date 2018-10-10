package test.skava.ling.lang;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.lang.String0;
import test.skava.SKUnit;

public class String0Test extends SKUnit {
  @Test
  public void upper2lower() throws Exception {
    Assert.assertEquals(String0.upper2lower("firstName"), "first_name");
  }

  @Test
  public void upper2lower2() throws Exception {
    Assert.assertEquals(String0.upper2lower("firstName", String0.UNDERLINE), "first_name");
  }

  @Test
  public void null2empty2() throws Exception {
    Assert.assertEquals(String0.null2empty2("firstName", ""), "firstName");
    Assert.assertEquals(String0.null2empty2("", "firstName"), "firstName");
  }

}
