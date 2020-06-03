package sktest.skava.time;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.time.LocalDate0;
import org.shaneking.skava.util.Date0;

import java.time.LocalDate;

public class LocalDate0Test {
  @Test
  public void now() {
    Assert.assertEquals(Date0.on().date(), LocalDate0.on().date());
  }

  @Test
  public void on() {
    Assert.assertEquals(Date0.on().date(), LocalDate0.on().date());
  }

  @Test
  public void testOn() {
    Assert.assertEquals(Date0.on().date(), LocalDate0.on().date());
  }

  @Test
  public void date() {
    Assert.assertEquals(Date0.on().date(), LocalDate0.on().date());
  }

  @Test
  public void format() {
    Assert.assertEquals(Date0.on().format(Date0.Y_M_D), LocalDate0.on().format(Date0.Y_M_D));
  }

  @Test
  public void parse() {
    Assert.assertNotNull(LocalDate0.on().parse("2010-01-15"));
  }

  @Test
  public void testParse() {
    Assert.assertNotNull(LocalDate0.on().parse(Date0.YMD, "20200115"));
  }

  @Test
  public void ymd() {
    Assert.assertEquals(Date0.on().ymd(), LocalDate0.on().ymd());
  }

  @Test
  public void ySmSd() {
    Assert.assertEquals(Date0.on().ySmSd(), LocalDate0.on().ySmSd());
  }

  @Test
  public void getLocalDate() {
    Assert.assertNotNull(LocalDate0.on().getLocalDate());
  }

  @Test
  public void setLocalDate() {
    Assert.assertNotNull(LocalDate0.on().setLocalDate(LocalDate.now()));
  }

  @Test
  public void testToString() {
    Assert.assertTrue(LocalDate0.on().toString().contains("LocalDate0("));
  }
}
