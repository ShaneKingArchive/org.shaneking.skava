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

import java.util.List;

public class List0 {
  public static <E> List<E> fillArrayList(E fillValue, int fillArraySize, List<E> fillArray) {
    List<E> rtn = Lists.newArrayList();
    fillArray = fillArray == null ? Lists.newArrayList() : fillArray;
    int minSize = Math.min(fillArraySize, fillArray.size());
    for (int i = 0; i < minSize; i++) {
      rtn.add(fillArray.get(i));
    }
    for (int i = minSize; i < fillArraySize; i++) {
      rtn.add(fillValue);
    }
    return rtn;
  }

  public static <M, N, E> List<E> operation(@NonNull List<M> firstList, @NonNull List<N> restList, Operation<? super M, ? super N, ? extends E> operation) {
    List<E> rtn = Lists.newArrayList();
    int maxSize = Math.min(firstList.size(), restList.size());
    List<M> fillFirstList = List0.fillArrayList(null, maxSize, firstList);
    List<N> fillRestList = List0.fillArrayList(null, maxSize, restList);
    for (int i = 0; i < maxSize; i++) {
      rtn.add(operation.calculate(firstList.get(i), restList.get(i)));
    }
    return rtn;
  }
}
