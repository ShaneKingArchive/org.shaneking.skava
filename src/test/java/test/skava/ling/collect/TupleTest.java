package test.skava.ling.collect;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.ling.lang.String0;
import test.skava.SKUnit;

public class TupleTest extends SKUnit
{
  private Tuple             eleven      = null;
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
    skPrint(eleven.toString(String0.DOT));
  }

  @Test
  public void toString1() throws Exception
  {
    skPrint(eleven.toString(String0.OPEN_BRACKET, String0.DOT, String0.CLOSE_BRACKET));
  }

  @Test
  public void toString2() throws Exception
  {
    skPrint(eleven.toString(tupleJoiner));
  }

  @Test
  public void joinWith0() throws Exception
  {
    skPrint(Tuple.joinWith(String0.DOT));
  }

  @Test
  public void joinWith1() throws Exception
  {
    skPrint(Tuple.joinWith(String0.OPEN_BRACKET, String0.DOT, String0.CLOSE_BRACKET));
  }

  @Test
  public void prepend() throws Exception
  {
    skPrint(eleven.prepend(0));
  }

  @Test
  public void of() throws Exception
  {
    skPrint(Tuple.of());
  }

  @Test
  public void of1() throws Exception
  {
    skPrint(Tuple.of(1));
  }

  @Test
  public void of2() throws Exception
  {
    skPrint(Tuple.of(1, 2));
  }

  @Test
  public void of3() throws Exception
  {
    skPrint(Tuple.of(1, 2, 3));
  }

  @Test
  public void of4() throws Exception
  {
    skPrint(Tuple.of(1, 2, 3, 4));
  }

  @Test
  public void of5() throws Exception
  {
    skPrint(Tuple.of(1, 2, 3, 4, 5));
  }

  @Test
  public void of6() throws Exception
  {
    skPrint(Tuple.of(1, 2, 3, 4, 5, 6));
  }

  @Test
  public void of7() throws Exception
  {
    skPrint(Tuple.of(1, 2, 3, 4, 5, 6, 7));
  }

  @Test
  public void of8() throws Exception
  {
    skPrint(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8));
  }

  @Test
  public void of9() throws Exception
  {
    skPrint(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
  }

  @Test
  public void of10() throws Exception
  {
    skPrint(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
  }

  @Test
  public void of11() throws Exception
  {
    skPrint(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
  }

  @Test
  public void getFirst() throws Exception
  {
    skPrint(Tuple.getFirst(eleven));
  }

  @Test
  public void getSecond() throws Exception
  {
    skPrint(Tuple.getSecond(eleven));
  }

  @Test
  public void getThird() throws Exception
  {
    skPrint(Tuple.getThird(eleven));
  }

  @Test
  public void getFourth() throws Exception
  {
    skPrint(Tuple.getFourth(eleven));
  }

  @Test
  public void getFifth() throws Exception
  {
    skPrint(Tuple.getFifth(eleven));
  }

  @Test
  public void getSixth() throws Exception
  {
    skPrint(Tuple.getSixth(eleven));
  }

  @Test
  public void getSeventh() throws Exception
  {
    skPrint(Tuple.getSeventh(eleven));
  }

  @Test
  public void getEighth() throws Exception
  {
    skPrint(Tuple.getEighth(eleven));
  }

  @Test
  public void getNinth() throws Exception
  {
    skPrint(Tuple.getNinth(eleven));
  }

  @Test
  public void getTenth() throws Exception
  {
    skPrint(Tuple.getTenth(eleven));
  }

  @Test
  public void getN() throws Exception
  {
    skPrint(Tuple.getN(eleven, 3));
  }
}
