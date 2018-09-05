/*
 * @(#)Operation.java		Created at 2018/9/4
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.ling.collect;

public interface Operation<M, N, E>
{
  E calculate(M m, N n);
}
