package sktest.skava.ling.lang;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.lang.String0;
import sktest.skava.SKUnit;

public class String0Test extends SKUnit {

  @Test
  public void newInstance() {
    Assert.assertNotNull(new String0().toString());
  }

  @Test
  public void null2empty2() {
    Assert.assertEquals(String0.null2empty2("firstName", ""), "firstName");
    Assert.assertEquals(String0.null2empty2("", "firstName"), "firstName");
  }

  @Test(expected = NullPointerException.class)
  public void null2empty2Null() {
    Assert.assertEquals(String0.null2empty2("firstName", null), "firstName");
  }

  @Test
  public void upper2lower() {
    Assert.assertEquals(String0.upper2lower("firstName"), "first_name");
  }

  @Test(expected = NullPointerException.class)
  public void upper2lowerNull() {
    Assert.assertEquals(String0.upper2lower(null), "first_name");
  }

  @Test
  public void upper2lower2() {
    Assert.assertEquals(String0.upper2lower("firstName", String0.UNDERLINE), "first_name");
  }

  @Test(expected = NullPointerException.class)
  public void upper2lower2Null1() {
    Assert.assertEquals(String0.upper2lower(null, String0.UNDERLINE), "first_name");
  }

  @Test(expected = NullPointerException.class)
  public void upper2lower2Null2() {
    Assert.assertEquals(String0.upper2lower("firstName", null), "first_name");
  }

}
