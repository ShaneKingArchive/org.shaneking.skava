package test.skava.sql;/*
 * @(#)SqlSuites.java		Created at 2017/9/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.skava.sql.entity.EntitySuites;
import test.skava.sql.parser.ParserSuites;

@RunWith(Suite.class)
@Suite.SuiteClasses({EntitySuites.class, ParserSuites.class})
public class SqlSuites {
}
