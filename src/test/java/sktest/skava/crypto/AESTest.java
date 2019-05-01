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

import java.security.NoSuchAlgorithmException;

public class AESTest extends SKUnit {

  private String plainText = "plainText";
  private String cipherText = "2SZ/de9I0rvxO7s9wdCohQ==";

  @Test
  public void singleton() throws NoSuchAlgorithmException {
    Assert.assertSame(AES.singleton(), AES.singleton());
  }

  @Test
  public void base64Bytes() {
    Assert.assertEquals(AES.base64(AES.DEFAULT_SALT.getBytes()), "NTQ2ODY5NzM0OTczNTM2OTc4NDY2Rjc1NzI0MjY5NzQ1MzYxNkM3NDQ2NkY3MjUzNjg2MTZFNjU0QjY5NkU2Nw==");
  }

  @Test(expected = NullPointerException.class)
  public void base64BytesNull() {
    byte[] b = null;
    Assert.assertEquals(AES.base64(b), "NTQ2ODY5NzM0OTczNTM2OTc4NDY2Rjc1NzI0MjY5NzQ1MzYxNkM3NDQ2NkY3MjUzNjg2MTZFNjU0QjY5NkU2Nw==");
  }

  @Test
  public void base64String() {
    Assert.assertEquals(new String(AES.base64(AES.DEFAULT_SALT)), "玼�������~���:�^��n6����~��.�㮅�nw�ε�N��\u001E��N�");
  }

  @Test(expected = NullPointerException.class)
  public void base64StringNull() {
    String s = null;
    Assert.assertEquals(new String(AES.base64(s)), "玼�������~���:�^��n6����~��.�㮅�nw�ε�N��\u001E��N�");
  }

  @Test
  public void decrypt1() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(cipherText), plainText);
  }

  @Test(expected = NullPointerException.class)
  public void decrypt1Null() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(null), plainText);
  }

  @Test
  public void decrypt2() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(cipherText, AES.DEFAULT_SALT), plainText);
  }

  @Test(expected = NullPointerException.class)
  public void decrypt2Null1() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(null, AES.DEFAULT_SALT), plainText);
  }

  @Test(expected = NullPointerException.class)
  public void decrypt2Null2() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(cipherText, null), plainText);
  }

  @Test
  public void decrypt3() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(cipherText, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32)), plainText);
  }

  @Test(expected = NullPointerException.class)
  public void decrypt3Null1() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(null, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32)), plainText);
  }

  @Test(expected = NullPointerException.class)
  public void decrypt3Null2() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(cipherText, null, AES.DEFAULT_SALT.substring(0, 32)), plainText);
  }

  @Test(expected = NullPointerException.class)
  public void decrypt3Null3() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(cipherText, AES.DEFAULT_SALT, null), plainText);
  }

  @Test
  public void decrypt4() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(cipherText, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32), AES.DEFAULT_SALT), plainText);
  }

  @Test(expected = NullPointerException.class)
  public void decrypt4Null1() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(null, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32), AES.DEFAULT_SALT), plainText);
  }

  @Test(expected = NullPointerException.class)
  public void decrypt4Null2() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(cipherText, null, AES.DEFAULT_SALT.substring(0, 32), AES.DEFAULT_SALT), plainText);
  }

  @Test(expected = NullPointerException.class)
  public void decrypt4Null3() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(cipherText, AES.DEFAULT_SALT, null, AES.DEFAULT_SALT), plainText);
  }

  @Test(expected = NullPointerException.class)
  public void decrypt4Null4() throws Exception {
    Assert.assertEquals(AES.singleton().decrypt(cipherText, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32), null), plainText);
  }

  @Test
  public void encrypt1() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(plainText), cipherText);
  }

  @Test(expected = NullPointerException.class)
  public void encrypt1Null() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(null), cipherText);
  }

  @Test
  public void encrypt2() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(plainText, AES.DEFAULT_SALT), cipherText);
  }

  @Test(expected = NullPointerException.class)
  public void encrypt2Null1() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(null, AES.DEFAULT_SALT), cipherText);
  }

  @Test(expected = NullPointerException.class)
  public void encrypt2Null2() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(plainText, null), cipherText);
  }

  @Test
  public void encrypt3() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(plainText, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32)), cipherText);
  }

  @Test(expected = NullPointerException.class)
  public void encrypt3Null1() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(null, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32)), cipherText);
  }

  @Test(expected = NullPointerException.class)
  public void encrypt3Null2() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(plainText, null, AES.DEFAULT_SALT.substring(0, 32)), cipherText);
  }

  @Test(expected = NullPointerException.class)
  public void encrypt3Null3() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(plainText, AES.DEFAULT_SALT, null), cipherText);
  }

  @Test
  public void encrypt4() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(plainText, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32), AES.DEFAULT_SALT), cipherText);
  }

  @Test(expected = NullPointerException.class)
  public void encrypt4Null1() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(null, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32), AES.DEFAULT_SALT), cipherText);
  }

  @Test(expected = NullPointerException.class)
  public void encrypt4Null2() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(plainText, null, AES.DEFAULT_SALT.substring(0, 32), AES.DEFAULT_SALT), cipherText);
  }

  @Test(expected = NullPointerException.class)
  public void encrypt4Null3() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(plainText, AES.DEFAULT_SALT, null, AES.DEFAULT_SALT), cipherText);
  }

  @Test(expected = NullPointerException.class)
  public void encrypt4Null4() throws Exception {
    Assert.assertEquals(AES.singleton().encrypt(plainText, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32), null), cipherText);
  }

  @Test
  public void hexBytes() {
    Assert.assertEquals(AES.hex(AES.DEFAULT_SALT.getBytes()), "35343638363937333439373335333639373834363646373537323432363937343533363136433734343636463732353336383631364536353442363936453637");
  }

  @Test(expected = NullPointerException.class)
  public void hexBytesNull() {
    byte[] b = null;
    Assert.assertEquals(AES.hex(b), "35343638363937333439373335333639373834363646373537323432363937343533363136433734343636463732353336383631364536353442363936453637");
  }

  @Test
  public void hexString() {
    Assert.assertEquals(new String(AES.hex(AES.DEFAULT_SALT)), "ThisIsSixFourBitSaltForShaneKing");
  }

  @Test(expected = IllegalStateException.class)
  public void hexStringException() {
    Assert.assertEquals(new String(AES.hex(AES.DEFAULT_SALT.substring(0, 17))), "ThisIsSixFourBitSaltForShaneKing");
  }

  @Test(expected = NullPointerException.class)
  public void hexStringNull() {
    String s = null;
    Assert.assertEquals(new String(AES.hex(s)), "ThisIsSixFourBitSaltForShaneKing");
  }
}
