package sktest.skava.ling.util;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.lang.String0;
import org.shaneking.skava.ling.util.List0;

import java.util.List;

public class List0Test {
  private List<String> emptyList3 = List0.fillList(String0.EMPTY, 3, null);

  @Test
  public void fillArrayList() {
    Assert.assertEquals(emptyList3, List0.fillList(String0.EMPTY, 3, null));
  }

  @Test
  public void newInstance() {
    Assert.assertNotNull(String.valueOf(new List0()));
  }

  @Test
  public void operation() {
    Assert.assertEquals(emptyList3, List0.calc(List0.fillList(String0.EMPTY, 3, null), List0.fillList(String0.EMPTY, 3, null), (m, n) -> {
      return m + n;
    }));
  }

  @Test(expected = NullPointerException.class)
  public void operationNull1() {
    Assert.assertEquals(emptyList3, List0.calc(null, List0.fillList(String0.EMPTY, 3, null), (m, n) -> {
      return m + n;
    }));
  }

  @Test(expected = NullPointerException.class)
  public void operationNull2() {
    Assert.assertEquals(emptyList3, List0.calc(List0.fillList(String0.EMPTY, 3, null), null, (m, n) -> {
      return m + n;
    }));
  }

}
