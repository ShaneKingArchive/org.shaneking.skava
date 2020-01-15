package sktest.skava.rr;

import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.rr.Ctx;
import org.shaneking.skava.rr.Resp;
import org.shaneking.skava.util.List0;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CtxTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testLanguage() throws Exception {
    List<Callable<String>> callableList = Lists.newArrayList();
    callableList.addAll(List0.fillList(null, 4 * Runtime.getRuntime().availableProcessors() + 1, () -> new AlwaysZhCn()));
    callableList.addAll(List0.fillList(null, 4 * Runtime.getRuntime().availableProcessors() + 1, () -> new AlwaysEnUs()));
    List<Future<String>> futureList = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors() + 1).invokeAll(callableList);
    long l = futureList.parallelStream().map(future -> {
      try {
        return future.get();
      } catch (Exception e) {
        e.printStackTrace();
        return Resp.CODE_UNKNOWN_EXCEPTION;
      }
    }).count();
    System.out.println(Runtime.getRuntime().availableProcessors());
    System.out.println(l);
    Assert.assertEquals(2 * (4 * Runtime.getRuntime().availableProcessors() + 1), l);
  }

  @Test
  public void testUserId() {
    Assert.assertNull(Ctx.USER_ID.get());
  }
}

class AlwaysZhCn implements Callable<String> {
  @Override
  public String call() {
    try {
      Ctx.LANGUAGE.set("zh-CN");
      Thread.sleep(1000);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      Assert.assertEquals("zh-CN", Ctx.LANGUAGE.get());
      return Ctx.LANGUAGE.get();
    }
  }
}

class AlwaysEnUs implements Callable<String> {
  @Override
  public String call() {
    try {
      Ctx.LANGUAGE.set("en-US");
      Thread.sleep(1010);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      Assert.assertEquals("en-US", Ctx.LANGUAGE.get());
      return Ctx.LANGUAGE.get();
    }
  }
}
