/*
 * @(#)List0.java		Created at 2018/9/4
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.ling.util;

import com.google.common.collect.Lists;
import lombok.NonNull;
import org.shaneking.skava.ling.collect.Operation;
import org.shaneking.skava.ling.collect.Tuple;

import java.util.List;

public class List0 {
  private static <E> Tuple.Pair<List<E>, Integer> fillList(List<E> fillList, int fillSize) {
    List<E> rtn = Lists.newArrayList();
    fillList = fillList == null ? Lists.newArrayList() : fillList;
    int minSize = Math.min(fillSize, fillList.size());
    for (int i = 0; i < minSize; i++) {
      rtn.add(fillList.get(i));
    }
    return Tuple.of(rtn, minSize);
  }

  public static <E> List<E> fillList(List<E> fillList, int fillSize, @NonNull Generator<E> generator) {
    Tuple.Pair<List<E>, Integer> pair = fillList(fillList, fillSize);
    List<E> rtn = Tuple.getFirst(pair);
    for (int i = Tuple.getSecond(pair); i < fillSize; i++) {
      rtn.add(generator.generate());
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

  public static <M, N, E> List<E> operation(@NonNull List<M> firstList, @NonNull List<N> restList, Operation<? super M, ? super N, ? extends E> operation) {
    List<E> rtn = Lists.newArrayList();
    int maxSize = Math.min(firstList.size(), restList.size());
    List<M> fillFirstList = List0.fillList(null, maxSize, firstList);
    List<N> fillRestList = List0.fillList(null, maxSize, restList);
    for (int i = 0; i < maxSize; i++) {
      rtn.add(operation.calculate(fillFirstList.get(i), fillRestList.get(i)));
    }
    return rtn;
  }
}
