package sktest.skava.util;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.lang.String0;
import org.shaneking.skava.util.List0;

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

  @Test
  public void splitByListSize() {
    Assert.assertEquals(Lists.newArrayList(Lists.newArrayList("1", "2"), Lists.newArrayList("3", "4"), Lists.newArrayList("5")), List0.splitByListSize(Lists.newArrayList("1", "2", "3", "4", "5"), 2));
  }

  @Test
  public void splitByListSizeLessThanTwo() {
    List<List<String>> expected = Lists.newArrayList();
    expected.add(Lists.newArrayList("1", "2", "3", "4", "5"));
    Assert.assertEquals(expected, List0.splitByListSize(Lists.newArrayList("1", "2", "3", "4", "5"), 1));
  }

  @Test
  public void splitByListTotal() {
    Assert.assertEquals(Lists.newArrayList(Lists.newArrayList("1", "3", "5"), Lists.newArrayList("2", "4")), List0.splitByListTotal(Lists.newArrayList("1", "2", "3", "4", "5"), 2));
  }

  @Test
  public void splitByListTotalLessThanTwo() {
    List<List<String>> expected = Lists.newArrayList();
    expected.add(Lists.newArrayList("1", "2", "3", "4", "5"));
    Assert.assertEquals(expected, List0.splitByListTotal(Lists.newArrayList("1", "2", "3", "4", "5"), 1));
  }
}
