package sktest.skava.ling.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.ling.lang.String0;
import org.shaneking.skava.ling.util.List0;

import java.util.List;

public class List0Test {
  List<String> emptyList3 = List0.fillArrayList(String0.EMPTY, 3, null);

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void fillArrayList() {
    Assert.assertEquals(emptyList3, List0.fillArrayList(String0.EMPTY, 3, null));
  }

  @Test
  public void newInstance() {
    Assert.assertNotNull(String.valueOf(new List0()));
  }

  @Test
  public void operation() {
    Assert.assertEquals(emptyList3, List0.operation(List0.fillArrayList(String0.EMPTY, 3, null), List0.fillArrayList(String0.EMPTY, 3, null), (m, n) -> {
      return m + n;
    }));
  }

  @Test(expected = NullPointerException.class)
  public void operationNull1() {
    Assert.assertEquals(emptyList3, List0.operation(null, List0.fillArrayList(String0.EMPTY, 3, null), (m, n) -> {
      return m + n;
    }));
  }

  @Test(expected = NullPointerException.class)
  public void operationNull2() {
    Assert.assertEquals(emptyList3, List0.operation(List0.fillArrayList(String0.EMPTY, 3, null), null, (m, n) -> {
      return m + n;
    }));
  }

}
