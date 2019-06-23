package sktest.skava.ling.lang;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.lang.Integer0;

public class Integer0Test {

  @Test
  public void gt2d() {
    Assert.assertEquals(6, Integer0.gt2d(8, 6));
    Assert.assertEquals(6, Integer0.gt2d(8, 6, 6));
  }

  @Test
  public void gte2d() {
    Assert.assertEquals(6, Integer0.gte2d(6, 6));
    Assert.assertEquals(6, Integer0.gte2d(6, 6, 6));
  }

  @Test
  public void lt2d() {
    Assert.assertEquals(6, Integer0.lt2d(5, 6));
    Assert.assertEquals(6, Integer0.lt2d(5, 6, 6));
  }

  @Test
  public void lte2d() {
    Assert.assertEquals(6, Integer0.lte2d(6, 6));
    Assert.assertEquals(6, Integer0.lte2d(6, 6, 6));
  }

  @Test
  public void null2Default() {
    Assert.assertEquals(6, Integer0.null2Default(null, 6));
    Assert.assertEquals(6, Integer0.null2Default(new Integer(6), 8));
  }

  @Test
  public void null2Zero() {
    Assert.assertEquals(0, Integer0.null2Zero(null));
    Assert.assertEquals(6, Integer0.null2Zero(new Integer(6)));
  }
}
