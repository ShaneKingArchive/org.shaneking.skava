package org.shaneking.skava.util;

import com.google.common.collect.Lists;
import lombok.NonNull;
import org.shaneking.skava.math.Calculable;
import org.shaneking.skava.persistence.Tuple;

import java.util.List;

public class List0 {
  private static <E> Tuple.Pair<List<E>, Integer> fillList(List<E> fillList, int fillSize) {
    List<E> reFillList = fillList == null ? Lists.newArrayList() : fillList;
    List<E> rtn = Lists.newArrayList();
    int minSize = Math.min(fillSize, reFillList.size());
    for (int i = 0; i < minSize; i++) {
      rtn.add(reFillList.get(i));
    }
    return Tuple.of(rtn, minSize);
  }

  public static <E> List<E> fillList(List<E> fillList, int fillSize, @NonNull Randomizer<E> randomizer) {
    Tuple.Pair<List<E>, Integer> pair = fillList(fillList, fillSize);
    List<E> rtn = Tuple.getFirst(pair);
    for (int i = Tuple.getSecond(pair); i < fillSize; i++) {
      rtn.add(randomizer.next());
    }
    return rtn;
  }

  public static <E> List<E> fillList(E fillValue, int fillSize, List<E> fillList) {
    Tuple.Pair<List<E>, Integer> pair = fillList(fillList, fillSize);
    List<E> rtn = Tuple.getFirst(pair);
    for (int i = Tuple.getSecond(pair); i < fillSize; i++) {
      rtn.add(fillValue);
    }
    return rtn;
  }

  public static <M, N, E> List<E> calc(@NonNull List<M> firstList, @NonNull List<N> restList, Calculable<? super M, ? super N, ? extends E> calculable) {
    List<E> rtn = Lists.newArrayList();
    int maxSize = Math.min(firstList.size(), restList.size());
    List<M> fillFirstList = List0.fillList(null, maxSize, firstList);
    List<N> fillRestList = List0.fillList(null, maxSize, restList);
    for (int i = 0; i < maxSize; i++) {
      rtn.add(calculable.calc(fillFirstList.get(i), fillRestList.get(i)));
    }
    return rtn;
  }
}
