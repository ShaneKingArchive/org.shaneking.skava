package sktest.skava.rr;

import org.junit.Test;
import org.shaneking.skava.rr.Pri;
import sktest.skava.SKUnit;

public class PriTest extends SKUnit {
  @Test
  public void build() {
    skPrint(Pri.build());
  }

  @Test
  public void build1() {
    skPrint(Pri.build(new Object()));
  }

  @Test
  public void build2() {
    skPrint(Pri.build(new Object(), new Object()));
  }

}
