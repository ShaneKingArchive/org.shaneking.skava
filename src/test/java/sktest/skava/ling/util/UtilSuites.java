/*
 * @(#)LingSutes.java		Created at 2018/2/3
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sktest.skava.ling.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sktest.skava.ling.util.concurrent.ConcurrentSuites;

@RunWith(Suite.class)
@Suite.SuiteClasses({ConcurrentSuites.class, Date0Test.class, List0Test.class, Random0Test.class, Regex0Test.class, UUID0Test.class})
public class UtilSuites {
}
