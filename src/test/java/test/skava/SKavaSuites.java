/*
 * @(#)SKavaTests.java		Created at 2018/2/3
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package test.skava;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.skava.crypto.CryptoSuites;
import test.skava.ling.LingSuites;
import test.skava.model.SKModelTest;
import test.skava.resp.RespSuites;
import test.skava.sql.SqlSuites;

@RunWith(Suite.class)
@Suite.SuiteClasses({CryptoSuites.class, LingSuites.class, SKModelTest.class, RespSuites.class, SqlSuites.class})
public class SKavaSuites {
}
