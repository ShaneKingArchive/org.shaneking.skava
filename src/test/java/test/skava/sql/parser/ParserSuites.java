/*
 * @(#)LingSutes.java		Created at 2018/2/3
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package test.skava.sql.parser;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({SensitiveExpressionReplacerTest.class, SensitiveItemsFinderTest.class, TableNamesFinderTest.class, TableNamesSelectReplacerTest.class})
public class ParserSuites {
}
