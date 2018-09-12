package test.skava.sql;/*
 * @(#)SqlSuites.java		Created at 2017/9/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.skava.sql.entity.SKEntityTest;
import test.skava.sql.entity.SKL10nEntityTest;
import test.skava.sql.entity.SKRefEntityTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({SKEntityTest.class, SKL10nEntityTest.class, SKRefEntityTest.class})
public class SqlSuites {
}
