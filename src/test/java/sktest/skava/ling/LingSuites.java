/*
 * @(#)LingSutes.java		Created at 2018/2/3
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sktest.skava.ling;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sktest.skava.ling.crypto.Cipher0Test;
import sktest.skava.ling.io.IOSuites;
import sktest.skava.ling.lang.LangSuites;
import sktest.skava.ling.security.SecuritySuites;
import sktest.skava.ling.util.UtilSuites;

@RunWith(Suite.class)
@Suite.SuiteClasses({Cipher0Test.class, IOSuites.class, LangSuites.class, SecuritySuites.class, UtilSuites.class})
public class LingSuites {
}
