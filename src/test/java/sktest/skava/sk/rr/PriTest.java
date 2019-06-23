package sktest.skava.sk.rr;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.sk.rr.Pri;
import org.shaneking.skava.t3.jackson.OM3;
import sktest.skava.SKUnit;

public class PriTest extends SKUnit {
  @Test
  public void build() {
    Assert.assertEquals("{}", OM3.writeValueAsString(Pri.build()));
  }

  @Test
  public void build1() {
    Assert.assertEquals("{}", OM3.writeValueAsString(Pri.build(new Object())));
  }

  @Test
  public void build2() {
    Assert.assertEquals("{}", OM3.writeValueAsString(Pri.build(new Object(), new Object())));
  }

}
