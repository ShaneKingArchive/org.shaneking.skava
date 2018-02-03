package test.skava.ling.collect;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.ling.lang.String0;
import test.skava.SKUnit;

public class TupleJoinerTest extends SKUnit
{
  private Tuple.Quadruple   ip          = null;
  private Tuple.TupleJoiner tupleJoiner = null;

  @Before
  public void setUp() throws Exception
  {
    super.setUp();
    ip = Tuple.of(127, 0, 0, 1);
    tupleJoiner = new Tuple.TupleJoiner(String0.OPEN_BRACKET, String0.DOT, String0.CLOSE_BRACKET);
  }

  @After
  public void tearDown() throws Exception
  {
    super.tearDown();
  }

  @Test
  public void appendTo0() throws Exception
  {
    skPrint(tupleJoiner.appendTo((Appendable) new StringBuilder(), ip));
  }

  @Test
  public void appendTo1() throws Exception
  {
    skPrint(tupleJoiner.appendTo(new StringBuilder(), ip));
  }

  @Test
  public void appendTo2() throws Exception
  {
    skPrint(tupleJoiner.appendTo(ip));
  }

  @Test
  public void join() throws Exception
  {
    skPrint(tupleJoiner.join(ip));
  }

}
