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

@RunWith(Parameterized.class)
public class Date0Test extends SKUnit {
  private Date date;
  private String pattern;

  private Date now = null;
  private String datetime = null;

  public Date0Test(Date date, String pattern) {
    super();
    this.date = date;
    this.pattern = pattern;
  }

  @Parameterized.Parameters
  public static List<Object[]> testExceptionParameters() {
    return Arrays.asList(new Object[][]{{new Date(), null}, {null, Date0.DATETIME}, {null, null}});
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    super.setUp();

    now = Date0.now();
    datetime = new SimpleDateFormat(Date0.DATETIME).format(now);
  }

  @Test
  public void date() throws Exception {
    Assert.assertEquals(Date0.on().date(), Date0.on(now).format(Date0.Y_M_D));
    Assert.assertNotEquals(Date0.on().date(), Date0.on(now).format(Date0.YMD));
  }

  @Test
  public void time() throws Exception {
    Assert.assertEquals(Date0.on().time(), Date0.on(now).format(Date0.H_MI_S));
    Assert.assertNotEquals(Date0.on().time(), Date0.on(now).format(Date0.HMIS));
  }

  @Test
  public void zone() throws Exception {
    Assert.assertEquals(Date0.on().zone(), Date0.on(now).format(Date0.XXX));
  }

  @Test
  public void dateTime() throws Exception {
    Assert.assertEquals(Date0.on().dateTime(), Date0.on(now).format(Date0.DATE_TIME));
  }

  @Test
  public void datetime() throws Exception {
    Assert.assertEquals(Date0.on().datetime(), Date0.on(now).format(Date0.DATETIME));
  }

  @Test
  public void now() throws Exception {
    Assert.assertEquals(Date0.now(), new Date());
  }

  @Test
  public void toString0() throws Exception {
    Assert.assertEquals(datetime, Date0.on(now).format(Date0.DATETIME));
  }

  @Test(expected = Exception.class)
  public void toStringWithException() {
    Date0.on(date).format(pattern);
  }

}
