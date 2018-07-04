/*
 * @(#)LingSutes.java		Created at 2018/2/3
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package test.skava.ling;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.skava.crypto.CryptoSuites;
import test.skava.ling.collect.CollectSuites;
import test.skava.ling.crypto.Cipher0Test;
import test.skava.ling.lang.String0Test;
import test.skava.ling.nio.Charset0Test;
import test.skava.ling.security.Key0Test;
import test.skava.ling.security.spec.KeySpec0Test;
import test.skava.ling.util.Date0Test;

@RunWith(Suite.class)
@Suite.SuiteClasses({CryptoSuites.class, CollectSuites.class, Cipher0Test.class, String0Test.class, Charset0Test.class, KeySpec0Test.class, Key0Test.class, Date0Test.class})
public class LingSuites
{
}
