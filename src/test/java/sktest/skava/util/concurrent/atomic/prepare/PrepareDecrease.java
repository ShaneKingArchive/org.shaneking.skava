package sktest.skava.util.concurrent.atomic.prepare;

import lombok.extern.slf4j.Slf4j;
import org.shaneking.skava.util.concurrent.atomic.AtomicLong0;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class PrepareDecrease implements Callable<Boolean> {
  private AtomicLong al;

  public PrepareDecrease(AtomicLong al) {
    this.al = al;
  }

  @Override
  public Boolean call() throws Exception {
    return AtomicLong0.tryDecreaseFailed(al, 0, 1);
  }
}
