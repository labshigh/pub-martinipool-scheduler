package com.labshigh.martinipool.core.helper;

import com.labshigh.martinipool.core.utils.AES256Util;
import java.util.UUID;

public class CryptoHelper {

  private static final int PASSWORD_PREFIX_LENGTH = 32;

  public static String encrypt(String plaintext) {

    UUID uuid = UUID.randomUUID();
    String encKey = uuid.toString().replace("-", "");

    try {
      AES256Util aes256Util = new AES256Util(encKey);
      String encPass = aes256Util.encrypt(plaintext);
      return encKey + encPass;
    } catch (Exception e) {
      return null;
    }
  }

  public static String decrypt(String encText) {

    String encKey = encText.substring(0, PASSWORD_PREFIX_LENGTH);

    try {
      AES256Util aes256Util = new AES256Util(encKey);
      String enc = encText.substring(PASSWORD_PREFIX_LENGTH);
      return aes256Util.decrypt(enc);
    } catch (Exception e) {
      return null;
    }
  }
}
