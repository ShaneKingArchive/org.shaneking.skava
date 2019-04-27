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
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void fillArrayList() throws Exception {
    Assert.assertEquals(emptyList3, List0.fillArrayList(String0.EMPTY, 3, null));
  }

  @Test
  public void operation() throws Exception {
    Assert.assertEquals(emptyList3, List0.operation(List0.fillArrayList(String0.EMPTY, 3, null), List0.fillArrayList(String0.EMPTY, 3, null), (m, n) -> {
      return m + n;
    }));
  }

}
