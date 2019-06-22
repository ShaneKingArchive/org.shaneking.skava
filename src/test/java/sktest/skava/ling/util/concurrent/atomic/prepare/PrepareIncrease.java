package sktest.skava.ling.util.concurrent.atomic.prepare;

import lombok.extern.slf4j.Slf4j;
import org.shaneking.skava.ling.util.concurrent.atomic.AtomicLong0;

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
    return AtomicLong0.tryIncreaseFailed(al, 20, 1);
  }
}
