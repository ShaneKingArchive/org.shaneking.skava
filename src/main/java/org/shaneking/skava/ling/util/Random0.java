/*
 * @(#)Random0.java		Created at 2018/7/18
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.ling.util;

import java.security.SecureRandom;

public class Random0 {
  private static final SecureRandom SR = new SecureRandom();

  public static int nextMaxInt(int max) {
    return Math.abs(SR.nextInt()) % max;
  }
}
