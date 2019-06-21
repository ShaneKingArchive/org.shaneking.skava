/*
 * @(#)AESTest.java		Created at 2017/5/26
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sktest.skava.crypto;

import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.crypto.AES;
import sktest.skava.SKUnit;

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

  @Test
  public void encrypt1() throws Exception {
    Assert.assertEquals(cipherText, AES.encrypt(plainText));
  }

  @Test
  public void encrypt2() throws Exception {
    Assert.assertEquals(cipherText, AES.encrypt(plainText, AES.DEFAULT_SALT));
  }

  @Test
  public void genKey() throws Exception {
    skPrint(AES.genKey());
    skPrint(AES.genKey());
    skPrint(AES.genKey());
  }

  @Test
  public void salt() {
    String salt = "ILoveYou";
    skPrint(Hex.encodeHexString(salt.getBytes()));
    Assert.assertEquals(16, Hex.encodeHexString(salt.getBytes()).length());
  }
}
