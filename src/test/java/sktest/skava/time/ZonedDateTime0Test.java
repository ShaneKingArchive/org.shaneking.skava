package sktest.skava.time;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.lang.String0;
import org.shaneking.skava.time.ZonedDateTime0;
import org.shaneking.skava.util.Date0;

import java.time.ZonedDateTime;

public class ZonedDateTime0Test {
  @Test
  public void now() {
    Assert.assertEquals(Date0.on().dateTimeZone(), ZonedDateTime0.on().dateTimeZone());
  }

  @Test
  public void on() {
    Assert.assertEquals(Date0.on().dateTimeZone(), ZonedDateTime0.on().dateTimeZone());
  }

  @Test
  public void testOn() {
    Assert.assertEquals(Date0.on().dateTimeZone(), ZonedDateTime0.on().dateTimeZone());
  }

  @Test
  public void date() {
    Assert.assertEquals(Date0.on().date(), ZonedDateTime0.on().date());
  }

  @Test
  public void dateTime() {
    Assert.assertEquals(Date0.on().dateTime(), ZonedDateTime0.on().dateTime());
  }

  @Test
  public void dateTimeZone() {
    Assert.assertEquals(Date0.on().dateTimeZone(), ZonedDateTime0.on().dateTimeZone());
  }

  @Test
  public void dateTimes() {
    Assert.assertTrue(String0.sameTotal(Date0.on().dateTimes(), ZonedDateTime0.on().dateTimes()) > ZonedDateTime0.on().dateTimes().length() - 3);
  }

  @Test
  public void dateTimesZone() {
    Assert.assertTrue(String0.sameTotal(Date0.on().dateTimesZone(), ZonedDateTime0.on().dateTimesZone()) > ZonedDateTime0.on().dateTimesZone().length() - 3);
  }

  @Test
  public void datetime() {
    Assert.assertEquals(Date0.on().datetime(), ZonedDateTime0.on().datetime());
  }

  @Test
  public void datetimes() {
    Assert.assertTrue(String0.sameTotal(Date0.on().datetimes(), ZonedDateTime0.on().datetimes()) > ZonedDateTime0.on().datetimes().length() - 3);
  }

  @Test
  public void format() {
    Assert.assertEquals(Date0.on().format(Date0.DATE_TIME_ZONE), ZonedDateTime0.on().format(Date0.DATE_TIME_ZONE));
  }
//  @Test
//  public void parse() {
//    Assert.assertNotNull(ZonedDateTime0.on().parse("2020-01-15 21:34:00.123"));
//  }
//
//  @Test
//  public void testParse() {
//    Assert.assertNotNull(ZonedDateTime0.on().parse(Date0.DATETIMESSS, "20200115213400123"));
//  }

  @Test
  public void time() {
    Assert.assertEquals(Date0.on().time(), ZonedDateTime0.on().time());
  }

  @Test
  public void timeZone() {
    Assert.assertEquals(Date0.on().timeZone(), ZonedDateTime0.on().timeZone());
  }

  @Test
  public void timeS() {
    Assert.assertTrue(String0.sameTotal(Date0.on().timeS(), ZonedDateTime0.on().timeS()) > ZonedDateTime0.on().timeS().length() - 3);
  }

  @Test
  public void timeSZone() {
    Assert.assertTrue(String0.sameTotal(Date0.on().timeSZone(), ZonedDateTime0.on().timeSZone()) > ZonedDateTime0.on().timeSZone().length() - 3);
  }

  @Test
  public void times() {
    Assert.assertTrue(String0.sameTotal(Date0.on().times(), ZonedDateTime0.on().times()) > ZonedDateTime0.on().times().length() - 3);
  }

  @Test
  public void ymd() {
    Assert.assertEquals(Date0.on().ymd(), ZonedDateTime0.on().ymd());
  }

  @Test
  public void ySmSd() {
    Assert.assertEquals(Date0.on().ySmSd(), ZonedDateTime0.on().ySmSd());
  }

  @Test
  public void zone() {
    Assert.assertEquals(Date0.on().zone(), ZonedDateTime0.on().zone());
  }

  @Test
  public void getZonedDateTime() {
    Assert.assertNotNull(ZonedDateTime0.on().getZonedDateTime());
  }

  @Test
  public void setZonedDateTime() {
    Assert.assertNotNull(ZonedDateTime0.on().setZonedDateTime(ZonedDateTime.now()));
  }

  @Test
  public void testToString() {
    Assert.assertTrue(ZonedDateTime0.on().toString().contains("ZonedDateTime0("));
  }
}
