package sktest.skava.lang;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.lang.Char0;

public class Char0Test {
  @Test
  public void isAlphabet() {
    Assert.assertTrue(Char0.isAlphabet(Char0.F));
    Assert.assertFalse(Char0.isAlphabet('1'));
    Assert.assertFalse(Char0.isAlphabet(Char0.C10));
  }

  @Test
  public void isDigital() {
    Assert.assertTrue(Char0.isDigital('1'));
    Assert.assertFalse(Char0.isDigital(Char0.N));
    Assert.assertFalse(Char0.isDigital(Char0.C13));
  }

  @Test
  public void isAlphabetOrDigital() {
    Assert.assertTrue(Char0.isAlphabetOrDigital('1'));
    Assert.assertTrue(Char0.isAlphabetOrDigital(Char0.T));
    Assert.assertFalse(Char0.isAlphabetOrDigital(Char0.C27));
  }
}
