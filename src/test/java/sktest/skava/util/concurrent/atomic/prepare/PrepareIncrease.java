package sktest.skava.util.concurrent.atomic.prepare;

import lombok.extern.slf4j.Slf4j;
import org.shaneking.skava.util.concurrent.atomic.AtomicLong0;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class PrepareIncrease implements Callable<Boolean> {
  private AtomicLong al;

  public PrepareIncrease(AtomicLong al) {
    this.al = al;
  }

  @Override
  public Boolean call() throws Exception {
    return AtomicLong0.tryIncreaseFailed(al, 2 * Runtime.getRuntime().availableProcessors() + 1, 1);
  }
}
