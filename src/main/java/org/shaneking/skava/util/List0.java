package org.shaneking.skava.util;

import com.google.common.collect.Lists;
import lombok.NonNull;
import org.shaneking.skava.math.Calculable;
import org.shaneking.skava.persistence.Tuple;

import java.util.List;

public class List0 {
  public static <M, N, E> List<E> calc(@NonNull List<M> firstList, @NonNull List<N> restList, @NonNull Calculable<? super M, ? super N, ? extends E> calculable) {
    List<E> rtn = Lists.newArrayList();
    int maxSize = Math.min(firstList.size(), restList.size());
    List<M> fillFirstList = List0.fillList(null, maxSize, firstList);
    List<N> fillRestList = List0.fillList(null, maxSize, restList);
    for (int i = 0; i < maxSize; i++) {
      rtn.add(calculable.calc(fillFirstList.get(i), fillRestList.get(i)));
    }
    return rtn;
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

  public static <E> List<List<E>> splitByListSize(@NonNull List<E> list, int size) {
    List<List<E>> rtnList = Lists.newArrayList();
    if (size < 2) {
      rtnList.add(list);
    } else {
      List<E> tmpList = Lists.newArrayList();
      for (int i = 0, j = 0; i < list.size(); i++, j++) {
        if (j == 0) {
          rtnList.add(tmpList);
        }
        tmpList.add(list.get(i));
        if (j == size - 1) {
          tmpList = Lists.newArrayList();
          j = -1;
        }
      }
    }
    return rtnList;
  }

  public static <E> List<List<E>> splitByListTotal(@NonNull List<E> list, int total) {
    List<List<E>> rtnList = Lists.newArrayList();
    if (total < 2) {
      rtnList.add(list);
    } else {
      rtnList = fillList(rtnList, total, Lists::newArrayList);
      for (int i = 0; i < list.size(); i++) {
        rtnList.get(i % total).add(list.get(i));
      }
    }
    return rtnList;
  }

  private static <E> Tuple.Pair<List<E>, Integer> fillList(List<E> fillList, int fillSize) {
    List<E> reFillList = fillList == null ? Lists.newArrayList() : fillList;
    List<E> rtn = Lists.newArrayList();
    int minSize = Math.min(fillSize, reFillList.size());
    for (int i = 0; i < minSize; i++) {
      rtn.add(reFillList.get(i));
    }
    return Tuple.of(rtn, minSize);
  }
}
