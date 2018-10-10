/*
 * @(#)SecuritySuites.java		Created at 2018/10/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package test.skava.ling.security;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.skava.ling.security.spec.KeySpec0Test;

@RunWith(Suite.class)
@Suite.SuiteClasses({Key0Test.class, KeySpec0Test.class})
public class SecuritySuites {
}
