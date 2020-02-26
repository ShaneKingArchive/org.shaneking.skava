package org.shaneking.skava.lang;

public class Boolean0 {
  public static String tf(boolean b) {
    return b ? String0.T : String0.F;
  }

  public static boolean tf(String s) {
    return String0.T.equalsIgnoreCase(s);
  }

  public static String yn(boolean b) {
    return b ? String0.Y : String0.N;
  }

  public static boolean yn(String s) {
    return String0.Y.equalsIgnoreCase(s);
  }
}
