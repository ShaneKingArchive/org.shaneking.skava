/*
 * @(#)RespSuites.java		Created at 2018/10/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package test.skava.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ReqTest.class, RespMesgTest.class, RespTest.class})
public class ModelSuites {
}