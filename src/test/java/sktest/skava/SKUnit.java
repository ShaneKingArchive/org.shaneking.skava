/*
 * @(#)SKUnit.java		Created at 2018/2/3
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sktest.skava;

import com.google.common.base.Stopwatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.shaneking.skava.ling.lang.String0;

public class SKUnit {
  @Rule
  public TestName testName = new TestName();
  private Stopwatch stopwatch = Stopwatch.createStarted();

  @Before
  public void setUp() {
    stopwatch = Stopwatch.createStarted();
  }

  @After
  public void tearDown() {
    System.out.println(testName.getMethodName() + String0.EQUAL + stopwatch.stop());
  }

  public void skPrint(Object o) {
    System.out.println(testName.getMethodName() + String0.EQUAL + o);
  }
}
