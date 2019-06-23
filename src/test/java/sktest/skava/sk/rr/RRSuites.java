/*
 * @(#)RespSuites.java		Created at 2018/10/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sktest.skava.sk.rr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({PriTest.class, ReqTest.class, RespTest.class})
public class RRSuites {
}
