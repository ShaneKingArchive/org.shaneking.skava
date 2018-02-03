/*
 * @(#)LingSutes.java		Created at 2018/2/3
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package test.skava.ling;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.skava.ling.collect.TupleSuites;
import test.skava.ling.util.Date0Test;

@RunWith(Suite.class)
@Suite.SuiteClasses({TupleSuites.class, Date0Test.class})
public class LingSuites
{
}
