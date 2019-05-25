package sktest.skava.ling.collect;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.ling.lang.String0;
import sktest.skava.SKUnit;

public class TupleJoinerTest extends SKUnit {
  private Tuple.Quadruple ip = null;
  private Tuple.TupleJoiner tupleJoiner = null;

  @Before
  public void setUp() {
    super.setUp();
    ip = Tuple.of(127, 0, 0, 1);
    tupleJoiner = new Tuple.TupleJoiner(String0.OPEN_BRACKET, String0.DOT, String0.CLOSE_BRACKET);
  }

  @After
  public void tearDown() {
    super.tearDown();
  }

  @Test
  public void appendTo1() {
    Assert.assertEquals(String.valueOf(tupleJoiner.appendTo(ip)), "[127.0.0.1]");
  }

  @Test
  public void appendTo2() {
    Assert.assertEquals(String.valueOf(tupleJoiner.appendTo(new StringBuffer(), ip)), "[127.0.0.1]");
  }

  @Test
  public void join() {
    Assert.assertEquals(tupleJoiner.join(ip), "[127.0.0.1]");
  }

  @Test
  public void testToString() {
    Assert.assertEquals("[127.[127,0,0,1].0.1]", tupleJoiner.join(Tuple.of(127, Tuple.of(127, 0, 0, 1), 0, 1)));
  }

}
