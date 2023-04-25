package com.labshigh.martinipool.core.utils;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.crypto.codec.Hex;

public class AES256Util {

  private final int KEY_LENGTH = 32;
  private final String CIPHER = "AES/CBC/PKCS5Padding";
  private final String iv;
  private final Key keySpec;

  /**
   * 16자리의 키값을 입력하여 객체를 생성한다.
   *
   * @param key 암/복호화를 위한 키값
   */
  public AES256Util(String key) throws KeyException {

    if (key.length() < 16) {
      throw new KeyException("The key length must be greater than 16.");
    }

    this.iv = key.substring(0, 16);
    this.keySpec = new SecretKeySpec(key.getBytes(), "AES");
  }

  /**
   * AES256 으로 암호화 한다.
   *
   * @param str 암호화할 문자열
   * @return
   * @throws NoSuchAlgorithmException
   * @throws GeneralSecurityException
   */
  public String encrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException {
    Cipher c = Cipher.getInstance(CIPHER);
    c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
    byte[] encrypted = c.doFinal(str.getBytes(StandardCharsets.UTF_8));
    return new String(Hex.encode(encrypted));
  }

  /**
   * AES256으로 암호화된 txt 를 복호화한다.
   *
   * @param str 복호화할 문자열
   * @return
   * @throws NoSuchAlgorithmException
   * @throws GeneralSecurityException
   */
  public String decrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException {
    Cipher c = Cipher.getInstance(CIPHER);
    c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
    byte[] byteStr = Hex.decode(str);
    return new String(c.doFinal(byteStr), StandardCharsets.UTF_8);
  }
}
