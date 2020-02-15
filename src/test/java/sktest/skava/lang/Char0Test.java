package sktest.skava.lang;

import com.google.common.base.Joiner;
import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.lang.Char0;
import org.shaneking.skava.util.Regex0;

public class Char0Test {
  @Test
  public void testAlphabet() {
    Assert.assertTrue(Char0.isAlphabet(Char0.F));
    Assert.assertFalse(Char0.isAlphabet('1'));
    Assert.assertFalse(Char0.isAlphabet(Char0.C10));
  }

  @Test
  public void testDigital() {
    Assert.assertTrue(Char0.isDigital('1'));
    Assert.assertFalse(Char0.isDigital(Char0.N));
    Assert.assertFalse(Char0.isDigital(Char0.C13));
  }

  @Test
  public void testAlphabetOrDigital() {
    Assert.assertTrue(Char0.isAlphabetOrDigital('1'));
    Assert.assertTrue(Char0.isAlphabetOrDigital(Char0.T));
    Assert.assertFalse(Char0.isAlphabetOrDigital(Char0.C27));
  }

  @Test
  public void testCxx() {
    String as = "\u001Ba\u001Bb\u001Bc\u001B";
    String bs = ",a,b,c";
    Assert.assertEquals("\u001B", String.valueOf(Char0.C27));
    Assert.assertEquals(bs, Joiner.on(",").join(as.split(String.valueOf(Char0.C27))));
    Assert.assertEquals(bs, Joiner.on(",").join(as.split(Regex0.ESCAPE)));
  }
}
