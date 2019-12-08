package sktest.skava.sk.crypto;

import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.lang.String0;
import org.shaneking.skava.ling.util.UUID0;
import org.shaneking.skava.sk.crypto.AES;
import sktest.skava.SKUnit;

import javax.crypto.BadPaddingException;
import java.util.UUID;

public class AESTest extends SKUnit {

  private String plainText = "plainText";
  private String cipherText = "urvlk7OI3tp6JUMD13lWTw==";

  @Test
  public void decrypt1() throws Exception {
    Assert.assertEquals(plainText, AES.decrypt(cipherText));
  }

  @Test
  public void decrypt2() throws Exception {
    Assert.assertEquals(plainText, AES.decrypt(cipherText, AES.DEFAULT_SALT));
  }

  @Test(expected = BadPaddingException.class)
  public void decrypt2Exception() throws Exception {
    Assert.assertNotEquals(plainText, AES.decrypt(cipherText, AES.genKey()));
  }

  @Test
  public void encrypt1() throws Exception {
    Assert.assertEquals(cipherText, AES.encrypt(plainText));
  }

  @Test
  public void encrypt2() throws Exception {
    Assert.assertEquals(cipherText, AES.encrypt(plainText, AES.DEFAULT_SALT));
    Assert.assertNotEquals(cipherText, AES.encrypt(plainText, AES.genKey()));
  }

  @Test
  public void genKey() throws Exception {
    Assert.assertEquals(16, AES.genKey().length());
  }

  @Test
  public void genKeyEightLength() throws Exception {
    skPrint(AES.genKey(UUID.randomUUID().toString().split(String0.MINUS)[0]));
    Assert.assertEquals(8, UUID.randomUUID().toString().split(String0.MINUS)[0].length());
  }

  @Test(expected = Exception.class)
  public void genKeyEmpty() throws Exception {
    Assert.assertEquals(String0.EMPTY, AES.genKey(String0.EMPTY));
  }

  @Test(expected = Exception.class)
  public void genKeyNotEightLength() throws Exception {
    Assert.assertEquals(String0.EMPTY, AES.genKey(UUID0.l19()));
  }

  @Test(expected = Exception.class)
  public void genKeyNull() throws Exception {
    Assert.assertEquals(String0.EMPTY, AES.genKey(null));
  }

  @Test
  public void salt() {
    String salt = "ILoveYou";
    Assert.assertEquals(16, Hex.encodeHexString(salt.getBytes()).length());
    Assert.assertEquals(AES.DEFAULT_SALT, Hex.encodeHexString(salt.getBytes()));
  }
}
