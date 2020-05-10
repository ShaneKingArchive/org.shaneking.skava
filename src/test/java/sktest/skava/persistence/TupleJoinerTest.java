package sktest.skava.persistence;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.lang.String0;
import org.shaneking.skava.persistence.Tuple;

public class TupleJoinerTest {
  private Tuple.Quadruple ip = null;
  private Tuple.TupleJoiner tupleJoiner = null;

  @Before
  public void setUp() {
    ip = Tuple.of(127, 0, 0, 1);
    tupleJoiner = new Tuple.TupleJoiner(String0.OPEN_BRACKET, String0.DOT, String0.CLOSE_BRACKET);
  }

  @After
  public void tearDown() {
  }

  @Test
  public void appendTo1() {
    Assert.assertEquals("[127.0.0.1]", String.valueOf(tupleJoiner.appendTo(ip)));
  }

  @Test
  public void appendTo2() {
    Assert.assertEquals("[127.0.0.1]", String.valueOf(tupleJoiner.appendTo(new StringBuffer(), ip)));
  }

  @Test
  public void join() {
    Assert.assertEquals("[127.0.0.1]", tupleJoiner.join(ip));
  }

  @Test
  public void testToString() {
    Assert.assertEquals("[127.[127,0,0,1].0.1]", tupleJoiner.join(Tuple.of(127, Tuple.of(127, 0, 0, 1), 0, 1)));
  }
}
