package sktest.skava.ling.util.concurrent.atomic;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.ling.util.List0;
import org.shaneking.skava.ling.util.Random0;
import sktest.skava.ling.util.concurrent.atomic.prepare.PrepareDecrease;
import sktest.skava.ling.util.concurrent.atomic.prepare.PrepareIncrease;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class AtomicLong0Test {

  final AtomicLong al = new AtomicLong(10);

  @Before
  public void setUp() throws Exception {
    al.set(10);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void tryDecreaseFailed() throws Exception {
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    List<Future<Boolean>> futureList = executorService.invokeAll(List0.fillList(null, 23, () -> new PrepareDecrease(al)));
    long l = futureList.parallelStream().map(future -> {
      try {
        return future.get();
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }).filter(b -> !b).count();
    System.out.println(l);
    Assert.assertEquals(13, l);
  }

  @Test
  public void tryIncreaseFailed() throws Exception {
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    List<Future<Boolean>> futureList = executorService.invokeAll(List0.fillList(null, 23, () -> new PrepareIncrease(al)));
    long l = futureList.parallelStream().map(future -> {
      try {
        return future.get();
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }).filter(b -> !b).count();
    System.out.println(l);
    Assert.assertEquals(13, l);
  }

  /**
   * Coverage[l = al.longValue();]
   */
  @Test
  public void tryDecreaseIncreaseFailed() throws Exception {
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    List<Future<Boolean>> futureList = executorService.invokeAll(List0.fillList(null, 32, () -> Random0.nextMaxInt(10) % 2 == 0 ? new PrepareDecrease(al) : new PrepareIncrease(al)));
    long l = futureList.parallelStream().map(future -> {
      try {
        return future.get();
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }).filter(b -> !b).count();
    System.out.println(l);
  }
}
