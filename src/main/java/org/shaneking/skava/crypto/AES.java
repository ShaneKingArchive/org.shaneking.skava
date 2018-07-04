/*
 * @(#)AES.java		Created at 2017/5/26
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.crypto;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.shaneking.skava.ling.crypto.Cipher0;
import org.shaneking.skava.ling.nio.Charset0;
import org.shaneking.skava.ling.security.Key0;
import org.shaneking.skava.ling.security.spec.KeySpec0;

import javax.annotation.Nonnull;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

public class AES
{
  //ThisIsSixFourBitSaltForShaneKing
  public static final String           DEFAULT_SALT     = "546869734973536978466F757242697453616C74466F725368616E654B696E67";
  private static      AES              instance         = null;
  private static      SecretKeyFactory secretKeyFactory = null;


  private AES() throws NoSuchAlgorithmException
  {
    secretKeyFactory = SecretKeyFactory.getInstance(KeySpec0.PBKDF2WithHmacSHA1);
  }

  public static synchronized AES singleton() throws NoSuchAlgorithmException
  {
    if (instance == null)
    {
      instance = new AES();
    }
    return instance;
  }

  public static String base64(byte[] bytes)
  {
    return Base64.encodeBase64String(bytes);
  }

  public static byte[] base64(@Nonnull String str)
  {
    return Base64.decodeBase64(str);
  }

  public static String hex(byte[] bytes)
  {
    return Hex.encodeHexString(bytes);
  }

  public static byte[] hex(@Nonnull String str)
  {
    try
    {
      return Hex.decodeHex(str.toCharArray());
    }
    catch (DecoderException e)
    {
      throw new IllegalStateException(e);
    }
  }

  public String decrypt(@Nonnull String cipherText) throws Exception
  {
    return decrypt(cipherText, DEFAULT_SALT);
  }

  public String decrypt(@Nonnull String cipherText, @Nonnull String salt) throws Exception
  {
    return decrypt(cipherText, salt, salt.substring(0, 32));
  }

  public String decrypt(@Nonnull String cipherText, @Nonnull String salt, @Nonnull String iv) throws Exception
  {
    return decrypt(cipherText, salt, iv, salt);
  }

  public String decrypt(@Nonnull String cipherText, @Nonnull String salt, @Nonnull String iv, @Nonnull String passPhrase) throws Exception
  {
    KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), hex(salt), salt.length(), salt.length() * 2);
    SecretKey secretKey = new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), Key0.AES);
    Cipher cipher = Cipher.getInstance(Cipher0.AES_CBC_PKCS5Padding);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(hex(iv)));
    return new String(cipher.doFinal(base64(cipherText)), Charset0.UTF_8);
  }

  public String encrypt(@Nonnull String cipherText) throws Exception
  {
    return encrypt(cipherText, DEFAULT_SALT);
  }

  public String encrypt(@Nonnull String cipherText, @Nonnull String salt) throws Exception
  {
    return encrypt(cipherText, salt, salt.substring(0, 32));
  }

  public String encrypt(@Nonnull String cipherText, @Nonnull String salt, @Nonnull String iv) throws Exception
  {
    return encrypt(cipherText, salt, iv, salt);
  }

  public String encrypt(@Nonnull String cipherText, @Nonnull String salt, @Nonnull String iv, @Nonnull String passPhrase) throws Exception
  {
    KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), hex(salt), salt.length(), salt.length() * 2);
    SecretKey secretKey = new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), Key0.AES);
    Cipher cipher = Cipher.getInstance(Cipher0.AES_CBC_PKCS5Padding);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(hex(iv)));
    return base64(cipher.doFinal(cipherText.getBytes(Charset0.UTF_8)));
  }
}
