/*
 * @(#)LingSutes.java		Created at 2018/2/3
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sktest.skava.ling;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sktest.skava.ling.collect.CollectSuites;
import sktest.skava.ling.crypto.Cipher0Test;
import sktest.skava.ling.lang.LangSuites;
import sktest.skava.ling.nio.Charset0Test;
import sktest.skava.ling.security.SecuritySuites;
import sktest.skava.ling.util.UtilSuites;

@RunWith(Suite.class)
@Suite.SuiteClasses({CollectSuites.class, Cipher0Test.class, LangSuites.class, Charset0Test.class, SecuritySuites.class, UtilSuites.class})
public class LingSuites {
}
