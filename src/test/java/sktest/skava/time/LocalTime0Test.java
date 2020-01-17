package sktest.skava.time;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.lang.String0;
import org.shaneking.skava.time.LocalTime0;
import org.shaneking.skava.util.Date0;

import java.time.LocalTime;

public class LocalTime0Test {
  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void now() {
    Assert.assertEquals(Date0.on().time(), LocalTime0.on().time());
  }

  @Test
  public void on() {
    Assert.assertEquals(Date0.on().time(), LocalTime0.on().time());
  }

  @Test
  public void testOn() {
    Assert.assertEquals(Date0.on().time(), LocalTime0.on().time());
  }

  @Test
  public void format() {
    Assert.assertEquals(Date0.on().format(Date0.H_MI_S), LocalTime0.on().format(Date0.H_MI_S));
  }

  @Test
  public void parse() {
    Assert.assertNotNull(LocalTime0.on().parse("20:50:00"));
  }

  @Test
  public void testParse() {
    Assert.assertNotNull(LocalTime0.on().parse(Date0.HMIS, "205000"));
  }

  @Test
  public void time() {
    Assert.assertEquals(Date0.on().time(), LocalTime0.on().time());
  }

  @Test
  public void timeS() {
    Assert.assertTrue(String0.sameTotal(Date0.on().timeS(), LocalTime0.on().timeS()) > LocalTime0.on().timeS().length() - 3);
  }

  @Test
  public void times() {
    Assert.assertTrue(String0.sameTotal(Date0.on().times(), LocalTime0.on().times()) > LocalTime0.on().times().length() - 3);
  }

  @Test
  public void getLocalTime() {
    Assert.assertNotNull(LocalTime0.on().getLocalTime());
  }

  @Test
  public void setLocalTime() {
    Assert.assertNotNull(LocalTime0.on().setLocalTime(LocalTime.now()));
  }

  @Test
  public void testToString() {
    Assert.assertTrue(LocalTime0.on().toString().contains("LocalTime0("));
  }
}
