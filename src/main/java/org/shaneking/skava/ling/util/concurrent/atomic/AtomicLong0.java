package org.shaneking.skava.ling.util.concurrent.atomic;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicLong0 {

  public static boolean tryDecreaseFailed(AtomicLong al, long min, long step) {
    long l = al.longValue();
    while (l > min) {
      if (al.compareAndSet(l, l - step)) {
        return true;
      }
      l = al.longValue();
    }
    return false;
  }

  public static boolean tryIncreaseFailed(AtomicLong al, long max, long step) {
    long l = al.longValue();
    while (l < max) {
      if (al.compareAndSet(l, l + step)) {
        return true;
      }
      l = al.longValue();
    }
    return false;
  }
}
