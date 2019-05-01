package sktest.skava.ling.collect;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.ling.lang.String0;
import sktest.skava.SKUnit;

public class TupleTest extends SKUnit {
  private Tuple eleven = Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
  private Tuple.TupleJoiner tupleJoiner = null;

  @Before
  public void setUp() {
    super.setUp();
    eleven = Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
    tupleJoiner = new Tuple.TupleJoiner(String0.OPEN_BRACKET, String0.DOT, String0.CLOSE_BRACKET);
  }

  @After
  public void tearDown() {
    super.tearDown();
  }

  @Test
  public void joinWith1() {
    Assert.assertEquals(Tuple.joinWith(String0.DOT).toString(), "TupleJoiner{open='(', separator='.', close=')'}");
  }

  @Test
  public void joinWith3() {
    Assert.assertEquals(Tuple.joinWith(String0.OPEN_BRACKET, String0.DOT, String0.CLOSE_BRACKET).toString(), "TupleJoiner{open='[', separator='.', close=']'}");
  }

  @Test
  public void of() {
    Assert.assertEquals(Tuple.of().toString(), "()");
  }

  @Test
  public void of1() {
    Assert.assertEquals(Tuple.of(1).toString(), "(1)");
  }

  @Test
  public void of2() {
    Assert.assertEquals(Tuple.of(1, 2).toString(), "(1,2)");
  }

  @Test
  public void of3() {
    Assert.assertEquals(Tuple.of(1, 2, 3).toString(), "(1,2,3)");
  }

  @Test
  public void of4() {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4).toString(), "(1,2,3,4)");
  }

  @Test
  public void of5() {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4, 5).toString(), "(1,2,3,4,5)");
  }

  @Test
  public void of6() {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4, 5, 6).toString(), "(1,2,3,4,5,6)");
  }

  @Test
  public void of7() {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4, 5, 6, 7).toString(), "(1,2,3,4,5,6,7)");
  }

  @Test
  public void of8() {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8).toString(), "(1,2,3,4,5,6,7,8)");
  }

  @Test
  public void of9() {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9).toString(), "(1,2,3,4,5,6,7,8,9)");
  }

  @Test
  public void of10() {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).toString(), "(1,2,3,4,5,6,7,8,9,10)");
  }

  @Test
  public void of11() {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11).toString(), "(1,2,3,4,5,6,7,8,9,10,11)");
  }

  @Test
  public void getFirst() {
    Assert.assertEquals(Tuple.getFirst(eleven).toString(), "1");
  }

  @Test
  public void getSecond() {
    Assert.assertEquals(Tuple.getSecond(eleven).toString(), "2");
  }

  @Test
  public void getThird() {
    Assert.assertEquals(Tuple.getThird(eleven).toString(), "3");
  }

  @Test
  public void getFourth() {
    Assert.assertEquals(Tuple.getFourth(eleven).toString(), "4");
  }

  @Test
  public void getFifth() {
    Assert.assertEquals(Tuple.getFifth(eleven).toString(), "5");
  }

  @Test
  public void getSixth() {
    Assert.assertEquals(Tuple.getSixth(eleven).toString(), "6");
  }

  @Test
  public void getSeventh() {
    Assert.assertEquals(Tuple.getSeventh(eleven).toString(), "7");
  }

  @Test
  public void getEighth() {
    Assert.assertEquals(Tuple.getEighth(eleven).toString(), "8");
  }

  @Test
  public void getNinth() {
    Assert.assertEquals(Tuple.getNinth(eleven).toString(), "9");
  }

  @Test
  public void getTenth() {
    Assert.assertEquals(Tuple.getTenth(eleven).toString(), "10");
  }

  @Test
  public void getN() {
    Assert.assertEquals(Tuple.getN(eleven, 3).toString(), "4");
  }

  @Test
  public void prepend() {
    Assert.assertEquals(eleven.prepend(0).toString(), "(0,(1,2,3,4,5,6,7,8,9,10,11))");
  }

  @Test
  public void testEquals11() {
    Assert.assertEquals(eleven, eleven);
  }

  @Test
  public void testEquals12() {
    Assert.assertEquals(Tuple.of(127, 0, 0, 1), Tuple.of(127, 0, 0, 1));
  }

  @Test
  public void testEquals21() {
    Assert.assertNotEquals(Tuple.of(127, 0, 0, 1), null);
  }

  @Test
  public void testEquals22() {
    Assert.assertEquals(Tuple.of(127, 0, 0, 1), Tuple.of(127, 0, 0, 1));
  }

  @Test
  public void testEquals31() {
    Assert.assertNotEquals(eleven, new Object());
  }

  @Test
  public void testEquals41() {
    Assert.assertNotEquals(eleven, Tuple.of(127, 0, 0, 1));
  }

  @Test
  public void testHashCode() {
    Assert.assertEquals(Tuple.of(127, 0, 0, 1).hashCode(), 4706979);
  }

  @Test
  public void toString0() {
    Assert.assertEquals(eleven.toString(String0.DOT), "(1.2.3.4.5.6.7.8.9.10.11)");
  }

  @Test
  public void toString1() {
    Assert.assertEquals(eleven.toString(String0.OPEN_BRACKET, String0.DOT, String0.CLOSE_BRACKET), "[1.2.3.4.5.6.7.8.9.10.11]");
  }

  @Test
  public void toString2() {
    Assert.assertEquals(eleven.toString(tupleJoiner), "[1.2.3.4.5.6.7.8.9.10.11]");
  }

}
