package org.shaneking.skava.ling.lang;

public class Integer0 {

  //greater than then default
  public static int gt2d(int v, int gt, int d) {
    return v > gt ? d : v;
  }

  //greater than equals then default
  public static int gte2d(int v, int gt, int d) {
    return v >= gt ? d : v;
  }

  //less than then default
  public static int lt2d(int v, int lt, int d) {
    return v < lt ? d : v;
  }

  //less than equals then default
  public static int lte2d(int v, int lt, int d) {
    return v <= lt ? d : v;
  }

  public static int null2Default(Integer i, int d) {
    return i == null ? d : i;
  }
}
