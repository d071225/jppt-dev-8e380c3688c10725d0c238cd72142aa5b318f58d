package com.hletong.mob.security;

import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncryptUtils
{
  private static final String DES = "DESede";

  public static String md5Encrypt(String src)
    throws UnsupportedEncodingException
  {
    return encrypt("MD5", src);
  }

  public static String shaEncrypt(String src)
    throws UnsupportedEncodingException
  {
    return encrypt("SHA", src);
  }

  public static String sha256Encrypt(String src)
    throws UnsupportedEncodingException
  {
    return encrypt("SHA-256", src);
  }

  public static String sha512Encrypt(String src)
    throws UnsupportedEncodingException
  {
    return encrypt("SHA-512", src);
  }

  public static SecretKey genDESKey()
  {
    try
    {
      KeyGenerator keygen = KeyGenerator.getInstance("DESede");
      keygen.init(112);
      return keygen.generateKey(); 
    } catch (Exception e) {
    	throw new SecurityException( e);
    }
  }

  public static String desEncrypt(String src, SecretKey key)
    throws UnsupportedEncodingException
  {
    return encrypt("DESede", src, key);
  }

  public static String desDecrypt(String src, SecretKey key)
    throws IOException
  {
    return decrypt("DESede", src, key);
  }
  
  /**
   * 兼容2.0的密码校验
   * @Title base64EncodePassword  
   * @Description
   * @param rawPass
   * @return
   */
//  public static String base64EncodePassword(String rawPass) {
//		String md5Pwd = "";
//		MessageDigest messageDigest;
//		try {
//			messageDigest = MessageDigest.getInstance("MD5");
//			sun.misc.BASE64Encoder baseEncoder = new sun.misc.BASE64Encoder();
//			md5Pwd = baseEncoder.encode(messageDigest.digest(rawPass.getBytes("utf-8")));
//		} catch (Exception e) {
//			throw new SecurityException(e);
//		}
//		return md5Pwd;
//	}

  public static String base64Encode(String rawPass) {
    String md5Pwd = "";
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance("MD5");
      md5Pwd =Base64.encodeToString(messageDigest.digest(rawPass.getBytes("utf-8")),Base64.DEFAULT);
    } catch (Exception e) {

      throw new SecurityException(e);
    }
    return md5Pwd;
  }


  private static byte[] encrypt(String algorithm, byte[] obj)
  {
    if (null == obj)
      return null;
    try {
      MessageDigest md = MessageDigest.getInstance(algorithm);
      return md.digest(obj); 
      } catch (Exception e) {
    throw new SecurityException(e);
      }
  }

  private static String encrypt(String algorithm, String src)
    throws UnsupportedEncodingException
  {
    if (null == src)
      return null;
    byte[] bytes = src.getBytes("utf-8");
    byte[] encodeBtyes = encrypt(algorithm, bytes);
    return EncodeUtils.encodeHex(encodeBtyes);
  }

  private static byte[] encrypt(String algorithm, byte[] obj, Key key)
  {
    if (null == obj)
      return null;
    try {
      Cipher cipher = null;
      if (algorithm.equals("DESede")) {
        cipher = Cipher.getInstance(algorithm);
      }
      else
      {
        throw new SecurityException("系统不支持" + algorithm + "加密算法");
      }
      cipher.init(1, key);
      return cipher.doFinal(obj);
    } catch (Exception e) {
      throw new SecurityException(e); } 
    
  }

  private static String encrypt(String algorithm, String src, Key key)
    throws UnsupportedEncodingException
  {
    if (null == src)
      return null;
    byte[] bytes = src.getBytes("utf-8");
    byte[] encodeBtyes = encrypt(algorithm, bytes, key);
    return EncodeUtils.encodeHex(encodeBtyes);
  }

  private static byte[] decrypt(String algorithm, byte[] obj, Key key)
  {
    if (null == obj)
      return null;
    try {
      Cipher cipher = null;
      if (algorithm.equals("DESede")) {
        cipher = Cipher.getInstance(algorithm);
      }
      else
      {
        throw new SecurityException("系统不支持" + algorithm + "加密算法");
      }
      cipher.init(2, key);
      return cipher.doFinal(obj);
    } catch (Exception e) {
      throw new SecurityException(e.getMessage());
    } 
  }

  private static String decrypt(String algorithm, String src, Key key)
    throws IOException
  {
    if (null == src)
      return null;
    byte[] bytes = EncodeUtils.decodeHex(src);
    byte[] decodeBtyes = decrypt(algorithm, bytes, key);
    return new String(decodeBtyes, "UnicodeLittleUnmarked");
  }
  
  public static void main(String[] args) {
	  try {
            System.out.println(EncryptUtils.md5Encrypt("888888"));
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}