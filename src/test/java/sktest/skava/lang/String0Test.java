package sktest.skava.lang;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.lang.String0;
import org.shaneking.skava.persistence.Tuple;
import org.shaneking.skava.persistence.Tuple.Triple;

import java.text.MessageFormat;
import java.util.List;

@Slf4j
public class String0Test {
  //https://book.douban.com/review/5082337/
  //http://www.hxen.com/englisharticle/yingyumeiwen/2018-06-04/498157.html
  private List<Triple<String, Object[], String>> tripleList = Lists.newArrayList(
    Triple.of("倘若{0}坚强，软弱给{1}看。", new Object[]{"不", "谁"}, "倘若不坚强，软弱给谁看。"),
    Triple.of("因为喜欢{0}，因为喜欢这个{1}，{2}买了这本书来读。", new Object[]{"李心洁", "书名", "第一时间"}, "因为喜欢李心洁，因为喜欢这个书名，第一时间买了这本书来读。"),
    Triple.of("有些{0}，即使过了{1}岁，还像个{2}一般。", new Object[]{"女人", 30, "孩子"}, "有些女人，即使过了30岁，还像个孩子一般。"),
    Triple.of("从奶茶的《{0}》，到心洁的《{1}》。", new Object[]{"我的不完美", "在我说愿意之前"}, "从奶茶的《我的不完美》，到心洁的《在我说愿意之前》。"),
    Triple.of("{0}跟{1}说，{2}那么{3}吧。", new Object[]{"常常有人", "我", "不用", "坚强"}, "常常有人跟我说，不用那么坚强吧。"),
    Triple.of("{0}{1}看{2}和{3}{4}走在{5}{6}的{7}。", new Object[]{"我", "喜欢", "老奶奶", "老公公", "牵手", "午后", "树荫下", "背景"}, "我喜欢看老奶奶和老公公牵手走在午后树荫下的背景。"),
    Triple.of("{0}，{1}？", new Object[]{"是什么样的爱让他们一起走过了无数的岁月", "一起经历了人生无数的起伏"}, "是什么样的爱让他们一起走过了无数的岁月，一起经历了人生无数的起伏？"),
    Triple.of("{0}让他们{1}，还可以{2}？", new Object[]{"什么样的爱", "在白头偕老的时刻", "依偎在彼此的生命里"}, "什么样的爱让他们在白头偕老的时刻，还可以依偎在彼此的生命里？"),
    Triple.of("{0}，{1}，{2}？", new Object[]{"那样的幸福", "那么深的爱", "我这一生会拥有吗"}, "那样的幸福，那么深的爱，我这一生会拥有吗？"),
    Triple.of("Make {0} as opposed to networking", new Object[]{"friends"}, "Make friends as opposed to networking"),
    Triple.of("{0} to {1} {2} {3} {4} {5}", new Object[]{"Volunteer", "help", "out", "those", "less", "fortunate"}, "Volunteer to help out those less fortunate"),
    Triple.of("Stop {0} life as a {1} and do {2} for their own sake", new Object[]{"treating", "competition", "things"}, "Stop treating life as a competition and do things for their own sake")
  );

  @Test
  public void newInstance() {
    Assert.assertNotNull(String.valueOf(new String0()));
  }

  @Test
  public void dbColumn2Field() {
    Assert.assertEquals("firstName", String0.dbColumn2Field("first_name"));
  }

  @Test(expected = NullPointerException.class)
  public void dbColumn2FieldNull() {
    log.info(String0.dbColumn2Field(null));
  }

  @Test
  public void dbColumn2Field2() {
    Assert.assertEquals("firstName", String0.dbColumn2Field("first_name", String0.UNDERLINE));
  }

  @Test(expected = NullPointerException.class)
  public void dbColumn2Field2Null1() {
    Assert.assertEquals("firstName", String0.dbColumn2Field(null, String0.UNDERLINE));
  }

  @Test(expected = NullPointerException.class)
  public void dbColumn2Field2Null2() {
    Assert.assertEquals("firstName", String0.dbColumn2Field("first_name", null));
  }

  @Test
  public void dbColumn2SetField() {
    Assert.assertEquals("FirstName", String0.dbColumn2SetField("first_name"));
  }

  @Test(expected = NullPointerException.class)
  public void dbColumn2SetFieldNull() {
    Assert.assertEquals("FirstName", String0.dbColumn2SetField(null));
  }

  @Test
  public void dbColumn2SetField2() {
    Assert.assertEquals("FirstName", String0.dbColumn2SetField("first_name", String0.UNDERLINE));
  }

  @Test(expected = NullPointerException.class)
  public void dbColumn2SetField2Null1() {
    Assert.assertEquals("FirstName", String0.dbColumn2SetField(null, String0.UNDERLINE));
  }

  @Test(expected = NullPointerException.class)
  public void dbColumn2SetField2Null2() {
    Assert.assertEquals("FirstName", String0.dbColumn2SetField("first_name", null));
  }

  @Test
  public void field2DbColumn() {
    Assert.assertEquals("first_name", String0.field2DbColumn("firstName"));
  }

  @Test(expected = NullPointerException.class)
  public void field2DbColumnNull() {
    Assert.assertEquals("first_name", String0.field2DbColumn(null));
  }

  @Test
  public void field2DbColumn2() {
    Assert.assertEquals("first_name", String0.field2DbColumn("firstName", String0.UNDERLINE));
  }

  @Test(expected = NullPointerException.class)
  public void field2DbColumn2Null1() {
    Assert.assertEquals("first_name", String0.field2DbColumn(null, String0.UNDERLINE));
  }

  public void field2DbColumn2Null2() {
    Assert.assertEquals("firstnullname", String0.field2DbColumn("firstName", null));
  }

  @Test
  public void formatCompareTestMessageFormat() {
    for (int i = 0; i < 10; i++) {
      for (Triple triple : tripleList) {
        Assert.assertEquals(Tuple.<String>getN(triple, 2), MessageFormat.format(Tuple.getN(triple, 0), Tuple.<Object[]>getN(triple, 1)));
      }
    }
  }

  @Test
  public void formatCompareTestString0() {
    for (int i = 0; i < 10; i++) {
      for (Triple triple : tripleList) {
        Assert.assertEquals(Tuple.<String>getN(triple, 2), String0.format(Tuple.getN(triple, 0), Tuple.<Object[]>getN(triple, 1)));
      }
    }
  }

  @Test
  public void notNull2empty2() {
    Assert.assertEquals(String0.EMPTY, String0.notNull2empty2("firstName", String0.EMPTY));
    Assert.assertEquals("NULL", String0.notNull2empty2("NULL", "firstName"));
  }

  @Test
  public void notNullOrEmpty2() {
    Assert.assertEquals(String0.EMPTY, String0.notNullOrEmpty2("firstName", String0.EMPTY));
    Assert.assertEquals("null", String0.notNullOrEmpty2("null", "firstName"));
  }

  @Test
  public void notNullOrEmptyTo() {
    Assert.assertEquals(String0.EMPTY, String0.notNullOrEmptyTo("firstName", String0.EMPTY));
    Assert.assertEquals(String0.EMPTY, String0.notNullOrEmptyTo(String0.EMPTY, "firstName"));
  }

  @Test
  public void null2empty2() {
    Assert.assertEquals("firstName", String0.null2empty2("firstName", String0.EMPTY));
    Assert.assertEquals("firstName", String0.null2empty2("NULL", "firstName"));
  }

  @Test
  public void nullOrEmpty2() {
    Assert.assertEquals("firstName", String0.nullOrEmpty2("firstName", String0.EMPTY));
    Assert.assertEquals("firstName", String0.nullOrEmpty2("null", "firstName"));
  }

  @Test
  public void nullOrEmptyTo() {
    Assert.assertEquals("firstName", String0.nullOrEmptyTo("firstName", String0.EMPTY));
    Assert.assertEquals("firstName", String0.nullOrEmptyTo(String0.EMPTY, "firstName"));
  }

  @Test(expected = NullPointerException.class)
  public void null2empty2Null() {
    Assert.assertEquals("firstName", String0.null2empty2("firstName", null));
  }
}
