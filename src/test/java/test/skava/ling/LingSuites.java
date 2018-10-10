/*
 * @(#)LingSutes.java		Created at 2018/2/3
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package test.skava.ling;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.skava.ling.collect.CollectSuites;
import test.skava.ling.crypto.Cipher0Test;
import test.skava.ling.lang.String0Test;
import test.skava.ling.nio.Charset0Test;
import test.skava.ling.security.SecuritySuites;
import test.skava.ling.util.UtilSuites;

@RunWith(Suite.class)
@Suite.SuiteClasses({CollectSuites.class, Cipher0Test.class, String0Test.class, Charset0Test.class, SecuritySuites.class, UtilSuites.class})
public class LingSuites {
}
