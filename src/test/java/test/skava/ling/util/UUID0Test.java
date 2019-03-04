/*
 * @(#)UUID0Test.java		Created at 2019/3/3
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package test.skava.ling.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.util.Random0;
import org.shaneking.skava.ling.util.UUID0;
import test.skava.SKUnit;

import java.util.Set;
import java.util.UUID;

public class UUID0Test extends SKUnit {

  @Test
  public void l19() throws Exception {
    int r = Random0.nextMaxInt(100000);
    Set<String> set = Sets.newHashSet();
    for (int i = 0; i < r; i++) {
      set.add(UUID0.l19());
    }
    Assert.assertEquals(r, set.size());
    Assert.assertEquals(19 * r, Joiner.on("").join(set).length());
  }

  @Test
  public void l191() throws Exception {
    int r = 2;
    Set<String> set = Sets.newHashSet();
    set.add(UUID0.l19(UUID.fromString("00000000-0000-0000-0000-000000000000")));
    set.add(UUID0.l19(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff")));
    Assert.assertEquals(r, set.size());
    Assert.assertEquals(19 * r, Joiner.on("").join(set).length());
    System.out.println(Joiner.on("").join(set));
  }
}
