/*
 * @(#)SKavaTests.java		Created at 2018/2/3
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sktest.skava;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sktest.skava.crypto.CryptoSuites;
import sktest.skava.ling.LingSuites;
import sktest.skava.rr.RRSuites;

@RunWith(Suite.class)
@Suite.SuiteClasses({CryptoSuites.class, LingSuites.class, RRSuites.class})
public class SKavaSuites {
}
