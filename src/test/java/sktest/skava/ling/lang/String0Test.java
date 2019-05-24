package sktest.skava.ling.lang;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.ling.collect.Tuple.Triple;
import org.shaneking.skava.ling.lang.String0;
import sktest.skava.SKUnit;

import java.text.MessageFormat;
import java.util.List;

public class String0Test extends SKUnit {

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
  public void null2empty2() {
    Assert.assertEquals(String0.null2empty2("firstName", ""), "firstName");
    Assert.assertEquals(String0.null2empty2("", "firstName"), "firstName");
  }

  @Test(expected = NullPointerException.class)
  public void null2empty2Null() {
    Assert.assertEquals(String0.null2empty2("firstName", null), "firstName");
  }

  @Test
  public void upper2lower() {
    Assert.assertEquals(String0.upper2lower("firstName"), "first_name");
  }

  @Test(expected = NullPointerException.class)
  public void upper2lowerNull() {
    Assert.assertEquals(String0.upper2lower(null), "first_name");
  }

  @Test
  public void upper2lower2() {
    Assert.assertEquals(String0.upper2lower("firstName", String0.UNDERLINE), "first_name");
  }

  @Test(expected = NullPointerException.class)
  public void upper2lower2Null1() {
    Assert.assertEquals(String0.upper2lower(null, String0.UNDERLINE), "first_name");
  }

  @Test(expected = NullPointerException.class)
  public void upper2lower2Null2() {
    Assert.assertEquals(String0.upper2lower("firstName", null), "first_name");
  }

}
