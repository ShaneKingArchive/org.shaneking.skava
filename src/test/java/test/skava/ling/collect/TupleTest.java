package test.skava.ling.collect;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.ling.lang.String0;
import test.skava.SKUnit;

public class TupleTest extends SKUnit
{
  private Tuple             eleven      = Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
  private Tuple.TupleJoiner tupleJoiner = null;

  @Before
  public void setUp() throws Exception
  {
    super.setUp();
    eleven = Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
    tupleJoiner = new Tuple.TupleJoiner(String0.OPEN_BRACKET, String0.DOT, String0.CLOSE_BRACKET);
  }

  @After
  public void tearDown() throws Exception
  {
    super.tearDown();
  }

  @Test
  public void toString0() throws Exception
  {
    Assert.assertEquals(eleven.toString(String0.DOT), "(1.2.3.4.5.6.7.8.9.10.11)");
  }

  @Test
  public void toString1() throws Exception
  {
    Assert.assertEquals(eleven.toString(String0.OPEN_BRACKET, String0.DOT, String0.CLOSE_BRACKET), "[1.2.3.4.5.6.7.8.9.10.11]");
  }

  @Test
  public void toString2() throws Exception
  {
    Assert.assertEquals(eleven.toString(tupleJoiner), "[1.2.3.4.5.6.7.8.9.10.11]");
  }

  @Test
  public void joinWith0() throws Exception
  {
    Assert.assertEquals(Tuple.joinWith(String0.DOT).toString(), "TupleJoiner{open='(', separator='.', close=')'}");
  }

  @Test
  public void joinWith1() throws Exception
  {
    Assert.assertEquals(Tuple.joinWith(String0.OPEN_BRACKET, String0.DOT, String0.CLOSE_BRACKET).toString(), "TupleJoiner{open='[', separator='.', close=']'}");
  }

  @Test
  public void prepend() throws Exception
  {
    Assert.assertEquals(eleven.prepend(0).toString(), "(0,(1,2,3,4,5,6,7,8,9,10,11))");
  }

  @Test
  public void of() throws Exception
  {
    Assert.assertEquals(Tuple.of().toString(), "()");
  }

  @Test
  public void of1() throws Exception
  {
    Assert.assertEquals(Tuple.of(1).toString(), "(1)");
  }

  @Test
  public void of2() throws Exception
  {
    Assert.assertEquals(Tuple.of(1, 2).toString(), "(1,2)");
  }

  @Test
  public void of3() throws Exception
  {
    Assert.assertEquals(Tuple.of(1, 2, 3).toString(), "(1,2,3)");
  }

  @Test
  public void of4() throws Exception
  {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4).toString(), "(1,2,3,4)");
  }

  @Test
  public void of5() throws Exception
  {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4, 5).toString(), "(1,2,3,4,5)");
  }

  @Test
  public void of6() throws Exception
  {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4, 5, 6).toString(), "(1,2,3,4,5,6)");
  }

  @Test
  public void of7() throws Exception
  {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4, 5, 6, 7).toString(), "(1,2,3,4,5,6,7)");
  }

  @Test
  public void of8() throws Exception
  {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8).toString(), "(1,2,3,4,5,6,7,8)");
  }

  @Test
  public void of9() throws Exception
  {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9).toString(), "(1,2,3,4,5,6,7,8,9)");
  }

  @Test
  public void of10() throws Exception
  {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).toString(), "(1,2,3,4,5,6,7,8,9,10)");
  }

  @Test
  public void of11() throws Exception
  {
    Assert.assertEquals(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11).toString(), "(1,2,3,4,5,6,7,8,9,10,11)");
  }

  @Test
  public void getFirst() throws Exception
  {
    Assert.assertEquals(Tuple.getFirst(eleven).toString(), "1");
  }

  @Test
  public void getSecond() throws Exception
  {
    Assert.assertEquals(Tuple.getSecond(eleven).toString(), "2");
  }

  @Test
  public void getThird() throws Exception
  {
    Assert.assertEquals(Tuple.getThird(eleven).toString(), "3");
  }

  @Test
  public void getFourth() throws Exception
  {
    Assert.assertEquals(Tuple.getFourth(eleven).toString(), "4");
  }

  @Test
  public void getFifth() throws Exception
  {
    Assert.assertEquals(Tuple.getFifth(eleven).toString(), "5");
  }

  @Test
  public void getSixth() throws Exception
  {
    Assert.assertEquals(Tuple.getSixth(eleven).toString(), "6");
  }

  @Test
  public void getSeventh() throws Exception
  {
    Assert.assertEquals(Tuple.getSeventh(eleven).toString(), "7");
  }

  @Test
  public void getEighth() throws Exception
  {
    Assert.assertEquals(Tuple.getEighth(eleven).toString(), "8");
  }

  @Test
  public void getNinth() throws Exception
  {
    Assert.assertEquals(Tuple.getNinth(eleven).toString(), "9");
  }

  @Test
  public void getTenth() throws Exception
  {
    Assert.assertEquals(Tuple.getTenth(eleven).toString(), "10");
  }

  @Test
  public void getN() throws Exception
  {
    Assert.assertEquals(Tuple.getN(eleven, 3).toString(), "4");
  }
}
