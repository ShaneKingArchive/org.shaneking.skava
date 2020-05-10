package sktest.skava.util.concurrent.atomic;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.util.List0;
import org.shaneking.skava.util.Random0;
import sktest.skava.util.concurrent.atomic.prepare.PrepareDecrease;
import sktest.skava.util.concurrent.atomic.prepare.PrepareIncrease;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class AtomicLong0Test {
    private final AtomicLong al = new AtomicLong(Runtime.getRuntime().availableProcessors() + 1);

    @Before
    public void setUp() {
        al.set(Runtime.getRuntime().availableProcessors() + 1);
    }

    @Test
    public void tryDecreaseFailed() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(6 * Runtime.getRuntime().availableProcessors() + 1);
    List<Future<Boolean>> futureList = executorService.invokeAll(List0.fillList(null, 8 * Runtime.getRuntime().availableProcessors() + 1, () -> new PrepareDecrease(al)));
    long l = futureList.parallelStream().map(future -> {
      try {
        return future.get();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return false;
      }
    }).filter(b -> !b).count();
    System.out.println(Runtime.getRuntime().availableProcessors());
    System.out.println(l);
    Assert.assertEquals(7 * Runtime.getRuntime().availableProcessors(), l);
  }

  @Test
  public void tryIncreaseFailed() throws Exception {
    ExecutorService executorService = Executors.newFixedThreadPool(6 * Runtime.getRuntime().availableProcessors() + 1);
    List<Future<Boolean>> futureList = executorService.invokeAll(List0.fillList(null, 8 * Runtime.getRuntime().availableProcessors() + 1, () -> new PrepareIncrease(al)));
    long l = futureList.parallelStream().map(future -> {
      try {
        return future.get();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return false;
      }
    }).filter(b -> !b).count();
    System.out.println(Runtime.getRuntime().availableProcessors());
    System.out.println(l);
    Assert.assertEquals(7 * Runtime.getRuntime().availableProcessors() + 1, l);
  }

  /**
   * Coverage[l = al.longValue();]
   */
  @Test
  public void tryDecreaseIncreaseFailed() throws Exception {
    ExecutorService executorService = Executors.newFixedThreadPool(6 * Runtime.getRuntime().availableProcessors() + 1);
    List<Future<Boolean>> futureList = executorService.invokeAll(List0.fillList(null, 8 * Runtime.getRuntime().availableProcessors() + 1, () -> Random0.nextMaxInt(10) % 2 == 0 ? new PrepareDecrease(al) : new PrepareIncrease(al)));
    long l = futureList.parallelStream().map(future -> {
      try {
        return future.get();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return false;
      }
    }).filter(b -> !b).count();
    System.out.println(Runtime.getRuntime().availableProcessors());
    System.out.println(l);
    Assert.assertTrue(l != 99);
  }
}
