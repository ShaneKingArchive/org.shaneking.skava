/*
 * @(#)AESTest.java		Created at 2017/5/26
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sktest.skava.crypto;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.crypto.AES;
import sktest.skava.SKUnit;

public class AESTest extends SKUnit {

  private String plainText = "plainText";
  private String cipherText = "2SZ/de9I0rvxO7s9wdCohQ==";

  @Test
  public void singleton() throws Exception {
    Assert.assertSame(AES.singleton(), AES.singleton());
  }

  @Test
  public void decrypt() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(cipherText), plainText);
  }

  @Test
  public void decrypt1() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(cipherText, AES.DEFAULT_SALT), plainText);
  }

  @Test
  public void decrypt2() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(cipherText, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32)), plainText);
  }

  @Test
  public void decrypt3() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(cipherText, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32), AES.DEFAULT_SALT), plainText);
  }

  @Test
  public void encrypt() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(plainText), cipherText);
  }

  @Test
  public void encrypt1() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(plainText, AES.DEFAULT_SALT), cipherText);
  }

  @Test
  public void encrypt2() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(plainText, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32)), cipherText);
  }

  @Test
  public void encrypt3() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(plainText, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32), AES.DEFAULT_SALT), cipherText);
  }

  @Test
  public void base64() throws Exception {
    Assert.assertEquals(new String(AES.base64(AES.DEFAULT_SALT)), "玼�������~���:�^��n6����~��.�㮅�nw�ε�N��\u001E��N�");
  }

  @Test
  public void base641() throws Exception {
    Assert.assertEquals(AES.base64(AES.DEFAULT_SALT.getBytes()), "NTQ2ODY5NzM0OTczNTM2OTc4NDY2Rjc1NzI0MjY5NzQ1MzYxNkM3NDQ2NkY3MjUzNjg2MTZFNjU0QjY5NkU2Nw==");
  }

  @Test
  public void hex() throws Exception {
    Assert.assertEquals(new String(AES.hex(AES.DEFAULT_SALT)), "ThisIsSixFourBitSaltForShaneKing");
  }

  @Test(expected = IllegalStateException.class)
  public void hexException() throws Exception {
    Assert.assertEquals(new String(AES.hex(AES.DEFAULT_SALT.substring(0, 17))), "ThisIsSixFourBitSaltForShaneKing");
  }

  @Test
  public void hex1() throws Exception {
    Assert.assertEquals(AES.hex(AES.DEFAULT_SALT.getBytes()), "35343638363937333439373335333639373834363646373537323432363937343533363136433734343636463732353336383631364536353442363936453637");
  }
}
