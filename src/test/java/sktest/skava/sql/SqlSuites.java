package sktest.skava.sql;/*
 * @(#)SqlSuites.java		Created at 2017/9/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sktest.skava.sql.entity.EntitySuites;

@RunWith(Suite.class)
@Suite.SuiteClasses({EntitySuites.class, Keyword0Test.class, OperationContentTest.class})
public class SqlSuites {
}
