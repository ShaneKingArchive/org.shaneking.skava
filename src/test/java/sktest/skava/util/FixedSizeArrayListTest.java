package sktest.skava.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.shaneking.skava.lang.String0;
import org.shaneking.skava.util.FixedSizeArrayList;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class FixedSizeArrayListTest {

  @Test
  public void add() {
    List<Integer> list = new FixedSizeArrayList<>(5);
    for (int i = 0; i < 7; i++) {
      list.add(i);
    }
    assertEquals("2;3;4;5;6", Joiner.on(String0.SEMICOLON).join(list));
  }

  @Test
  public void addWithIndex() {
    List<Integer> list = new FixedSizeArrayList<>(5);
    for (int i = 0; i < 7; i++) {
      if (i > 2) {
        list.add(1, i);
      } else {
        list.add(i);
      }
    }
    assertEquals("5;6;3;1;2", Joiner.on(String0.SEMICOLON).join(list));
  }

  @Test
  public void addAllFillSmall() {
    List<Integer> list = new FixedSizeArrayList<>(5);
    for (int i = 0; i < 5; i++) {
      list.add(i);
    }
    list.addAll(Lists.newArrayList(11, 12, 13));
    assertEquals("3;4;11;12;13", Joiner.on(String0.SEMICOLON).join(list));
  }

  @Test
  public void addAllSmall() {
    List<Integer> list = new FixedSizeArrayList<>(5);
    for (int i = 0; i < 1; i++) {
      list.add(i);
    }
    list.addAll(Lists.newArrayList(11, 12, 13));
    assertEquals("0;11;12;13", Joiner.on(String0.SEMICOLON).join(list));
  }

  @Test
  public void addAllSmallOver() {
    List<Integer> list = new FixedSizeArrayList<>(5);
    for (int i = 0; i < 3; i++) {
      list.add(i);
    }
    list.addAll(Lists.newArrayList(11, 12, 13));
    assertEquals("1;2;11;12;13", Joiner.on(String0.SEMICOLON).join(list));
  }

  @Test
  public void addAllFillBig() {
    List<Integer> list = new FixedSizeArrayList<>(5);
    for (int i = 0; i < 5; i++) {
      list.add(i);
    }
    list.addAll(Lists.newArrayList(11, 12, 13, 14, 15, 16, 17));
    assertEquals("11;12;13;14;15;16;17", Joiner.on(String0.SEMICOLON).join(list));
  }

  @Test
  public void addAllWithIndexFillSmall() {
    for (int j = 0; j < 5; j++) {
      List<Integer> list = new FixedSizeArrayList<>(5);
      for (int i = 0; i < 5; i++) {
        list.add(i);
      }
      list.addAll(j, Lists.newArrayList(11, 12, 13));
      switch (j) {
        case 0:
          assertEquals("11;12;13;3;4", Joiner.on(String0.SEMICOLON).join(list));
          break;
        case 1:
          assertEquals("3;11;12;13;4", Joiner.on(String0.SEMICOLON).join(list));
          break;
        case 2:
        case 3:
        case 4:
          assertEquals("3;4;11;12;13", Joiner.on(String0.SEMICOLON).join(list));
          break;
        default:
          System.out.println("switch default");
          break;
      }
    }
  }

  @Test
  public void addAllWithIndexSmall() {
    for (int j = 0; j < 5; j++) {
      List<Integer> list = new FixedSizeArrayList<>(5);
      for (int i = 0; i < 1; i++) {
        list.add(i);
      }
      list.addAll(j, Lists.newArrayList(11, 12, 13));
      switch (j) {
        case 0:
          assertEquals("11;12;13;0", Joiner.on(String0.SEMICOLON).join(list));
          break;
        case 1:
        case 2:
        case 3:
        case 4:
          assertEquals("0;11;12;13", Joiner.on(String0.SEMICOLON).join(list));
          break;
        default:
          System.out.println("switch default");
          break;
      }
    }
  }


  @Test
  public void addAllWithIndexSmallOver() {
    for (int j = 0; j < 5; j++) {
      List<Integer> list = new FixedSizeArrayList<>(5);
      for (int i = 0; i < 3; i++) {
        list.add(i);
      }
      list.addAll(j, Lists.newArrayList(11, 12, 13));
      switch (j) {
        case 0:
          assertEquals("11;12;13;1;2", Joiner.on(String0.SEMICOLON).join(list));
          break;
        case 1:
          assertEquals("1;11;12;13;2", Joiner.on(String0.SEMICOLON).join(list));
          break;
        case 2:
        case 3:
        case 4:
          assertEquals("1;2;11;12;13", Joiner.on(String0.SEMICOLON).join(list));
          break;
        default:
          System.out.println("switch default");
          break;
      }
    }
  }

  @Test
  public void addAllWithIndexFillBig() {
    for (int j = 0; j < 5; j++) {
      List<Integer> list = new FixedSizeArrayList<>(5);
      for (int i = 0; i < 5; i++) {
        list.add(i);
      }
      list.addAll(j, Lists.newArrayList(11, 12, 13, 14, 15, 16, 17));
      assertEquals("11;12;13;14;15;16;17", Joiner.on(String0.SEMICOLON).join(list));
    }
  }

  @Test
  public void subList00() {
    List<Integer> list = Lists.newArrayList(0, 1, 2);
    assertEquals(Lists.newArrayList(), list.subList(0, 0));
    assertEquals(Lists.newArrayList(0), list.subList(0, 1));
  }

  @Test
  public void addIndexSize() {
    List<Integer> list = Lists.newArrayList(0, 1, 2);
    list.add(list.size(), 4);
    assertEquals(Lists.newArrayList(0, 1, 2, 4), list);
  }
}
