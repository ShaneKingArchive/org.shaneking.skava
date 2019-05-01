package sktest.skava.ling.util;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.shaneking.skava.ling.util.Date0;
import sktest.skava.SKUnit;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//Parameterized step1:add Parameterized.class to RunWith
@RunWith(Parameterized.class)
public class Date0Test extends SKUnit {
  private Date date;
  private String pattern;

  private Date now = null;
  private String datetime = null;

  //Parameterized step2:use step2 data to constructor object
  public Date0Test(Date date, String pattern) {
    super();
    this.date = date;
    this.pattern = pattern;
  }

  //Parameterized step2:static method return collection
  @Parameterized.Parameters
  public static List<Object[]> Date0TestParameters() {
    return Arrays.asList(new Object[][]{{new Date(), null}, {null, Date0.DATETIME}, {null, null}});
  }

  @BeforeClass
  public static void setUpBeforeClass() {
  }

  @AfterClass
  public static void tearDownAfterClass() {
  }

  @Before
  public void setUp() {
    super.setUp();

    now = Date0.now();
    datetime = new SimpleDateFormat(Date0.DATETIME).format(now);
  }

  @Test
  public void date() {
    Assert.assertEquals(Date0.on().date(), Date0.on(now).format(Date0.Y_M_D));
    Assert.assertNotEquals(Date0.on().date(), Date0.on(now).format(Date0.YMD));
  }

  @Test
  public void dateTime() {
    Assert.assertEquals(Date0.on().dateTime(), Date0.on(now).format(Date0.DATE_TIME));
  }

  @Test
  public void datetime() {
    Assert.assertEquals(Date0.on().datetime(), Date0.on(now).format(Date0.DATETIME));
  }

  @Test
  public void format() {
    Assert.assertEquals(datetime, Date0.on(now).format(Date0.DATETIME));
  }

  @Test(expected = Exception.class)
  public void formatNull() {
    Date0.on(date).format(pattern);
  }

  @Test
  public void now() {
    Assert.assertEquals(Date0.now(), new Date());
  }

  @Test
  public void time() {
    Assert.assertEquals(Date0.on().time(), Date0.on(now).format(Date0.H_MI_S));
    Assert.assertNotEquals(Date0.on().time(), Date0.on(now).format(Date0.HMIS));
  }

  @Test
  public void zone() {
    Assert.assertEquals(Date0.on().zone(), Date0.on(now).format(Date0.XXX));
  }

}
