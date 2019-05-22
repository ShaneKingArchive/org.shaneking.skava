package sktest.skava.ling.util;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.util.Array0;

public class Array0Test {

  @Test
  public void ofInteger() {
    Assert.assertArrayEquals(new int[]{1}, Array0.of(1));
  }

  @Test
  public void ofInteger2() {
    Assert.assertArrayEquals(new int[]{1, 2}, Array0.of(1, 2));
  }

  @Test
  public void ofString() {
    Assert.assertArrayEquals(new String[]{"1", "2"}, Array0.of("1", "2"));
  }

}
