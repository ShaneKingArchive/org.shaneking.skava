package sktest.skava.time;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.lang.String0;
import org.shaneking.skava.time.LocalDateTime0;
import org.shaneking.skava.util.Date0;

import java.time.LocalDateTime;

public class LocalDateTime0Test {
  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void now() {
    Assert.assertEquals(Date0.on().dateTime(), LocalDateTime0.on().dateTime());
  }

  @Test
  public void on() {
    Assert.assertEquals(Date0.on().dateTime(), LocalDateTime0.on().dateTime());
  }

  @Test
  public void testOn() {
    Assert.assertEquals(Date0.on().dateTime(), LocalDateTime0.on().dateTime());
  }

  @Test
  public void date() {
    Assert.assertEquals(Date0.on().date(), LocalDateTime0.on().date());
  }

  @Test
  public void dateTime() {
    Assert.assertEquals(Date0.on().dateTime(), LocalDateTime0.on().dateTime());
  }

  @Test
  public void dateTimes() {
    Assert.assertTrue(String0.sameTotal(Date0.on().dateTimes(), LocalDateTime0.on().dateTimes()) > LocalDateTime0.on().dateTimes().length() - 3);
  }

  @Test
  public void datetime() {
    Assert.assertEquals(Date0.on().datetime(), LocalDateTime0.on().datetime());
  }

  @Test
  public void datetimes() {
    Assert.assertTrue(String0.sameTotal(Date0.on().datetimes(), LocalDateTime0.on().datetimes()) > LocalDateTime0.on().datetimes().length() - 3);
  }

  @Test
  public void format() {
    Assert.assertEquals(Date0.on().format(Date0.DATE_TIME), LocalDateTime0.on().format(Date0.DATE_TIME));
  }

  @Test
  public void parse() {
    Assert.assertNotNull(LocalDateTime0.on().parse("2010-01-15 20:50:00"));
  }

  @Test
  public void testParse() {
    Assert.assertNotNull(LocalDateTime0.on().parse(Date0.DATETIME, "20200115205000"));
  }

  @Test
  public void time() {
    Assert.assertEquals(Date0.on().time(), LocalDateTime0.on().time());
  }

  @Test
  public void timeS() {
    Assert.assertTrue(String0.sameTotal(Date0.on().timeS(), LocalDateTime0.on().timeS()) > LocalDateTime0.on().timeS().length() - 3);
  }

  @Test
  public void times() {
    Assert.assertTrue(String0.sameTotal(Date0.on().times(), LocalDateTime0.on().times()) > LocalDateTime0.on().times().length() - 3);
  }

  @Test
  public void ymd() {
    Assert.assertEquals(Date0.on().ymd(), LocalDateTime0.on().ymd());
  }

  @Test
  public void ySmSd() {
    Assert.assertEquals(Date0.on().ySmSd(), LocalDateTime0.on().ySmSd());
  }

  @Test
  public void getLocalDateTime() {
    Assert.assertNotNull(LocalDateTime0.on().getLocalDateTime());
  }

  @Test
  public void setLocalDateTime() {
    Assert.assertNotNull(LocalDateTime0.on().setLocalDateTime(LocalDateTime.now()));
  }

  @Test
  public void testToString() {
    Assert.assertTrue(LocalDateTime0.on().toString().contains("LocalDateTime0("));
  }
}
