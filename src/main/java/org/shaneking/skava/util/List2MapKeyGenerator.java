package org.shaneking.skava.util;

public interface List2MapKeyGenerator<K, I> {
  K genKey(int idx, I item);
}
