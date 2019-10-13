/*
 * @(#)SKavaTests.java		Created at 2018/2/3
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sktest.skava.sk;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sktest.skava.sk.collect.CollectSuites;
import sktest.skava.sk.crypto.CryptoSuites;
import sktest.skava.sk.lang.LangSuites;
import sktest.skava.sk.rr.RRSuites;

@RunWith(Suite.class)
@Suite.SuiteClasses({CollectSuites.class, CryptoSuites.class, LangSuites.class, RRSuites.class})
public class SKSuites {
}
