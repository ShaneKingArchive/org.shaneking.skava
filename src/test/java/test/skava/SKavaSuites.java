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
import test.skava.model.ModelSuites;
import test.skava.sql.SqlSuites;

@RunWith(Suite.class)
@Suite.SuiteClasses({CryptoSuites.class, LingSuites.class, ModelSuites.class, SqlSuites.class})
public class SKavaSuites {
}
