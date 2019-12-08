package org.shaneking.skava.ling.util;

import java.security.SecureRandom;

public class Random0 {
  private static final SecureRandom SR = new SecureRandom();

  public static int nextMaxInt(int max) {
    return Math.abs(SR.nextInt()) % max;
  }
}
