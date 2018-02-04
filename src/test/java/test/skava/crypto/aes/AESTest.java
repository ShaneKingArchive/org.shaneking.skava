/*
 * @(#)AESTest.java		Created at 2017/5/26
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package test.skava.crypto.aes;

import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.crypto.AES;
import test.skava.SKUnit;

public class AESTest extends SKUnit
{

  private String plainText  = "plainText";
  private String cipherText = "2SZ/de9I0rvxO7s9wdCohQ==";

  @Test
  public void ins() throws Exception
  {
    Assert.assertSame(AES.ins(),AES.ins());
  }

  @Test
  public void decrypt() throws Exception
  {
    Assert.assertEquals(AES.ins().decrypt(cipherText), plainText);
  }

  @Test
  public void decrypt1() throws Exception
  {
    Assert.assertEquals(AES.ins().decrypt(cipherText, AES.DEFAULT_SALT), plainText);
  }

  @Test
  public void decrypt2() throws Exception
  {
    Assert.assertEquals(AES.ins().decrypt(cipherText, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32)), plainText);
  }

  @Test
  public void decrypt3() throws Exception
  {
    Assert.assertEquals(AES.ins().decrypt(cipherText, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32), AES.DEFAULT_SALT), plainText);
  }

  @Test
  public void encrypt() throws Exception
  {
    Assert.assertEquals(AES.ins().encrypt(plainText), cipherText);
  }

  @Test
  public void encrypt1() throws Exception
  {
    Assert.assertEquals(AES.ins().encrypt(plainText, AES.DEFAULT_SALT), cipherText);
  }

  @Test
  public void encrypt2() throws Exception
  {
    Assert.assertEquals(AES.ins().encrypt(plainText, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32)), cipherText);
  }

  @Test
  public void encrypt3() throws Exception
  {
    Assert.assertEquals(AES.ins().encrypt(plainText, AES.DEFAULT_SALT, AES.DEFAULT_SALT.substring(0, 32), AES.DEFAULT_SALT), cipherText);
  }

  @Test
  public void base64() throws Exception
  {
    skPrint(AES.base64(AES.DEFAULT_SALT));
  }

  @Test
  public void base641() throws Exception
  {
    skPrint(AES.base64(AES.DEFAULT_SALT.getBytes()));
  }

  @Test
  public void hex() throws Exception
  {
    skPrint(AES.hex(AES.DEFAULT_SALT));
  }

  @Test
  public void hex1() throws Exception
  {
    skPrint(AES.hex(AES.DEFAULT_SALT.getBytes()));
  }
}
