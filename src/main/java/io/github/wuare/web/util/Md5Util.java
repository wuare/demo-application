package io.github.wuare.web.util;

import io.github.wuare.web.exception.ServiceException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    private static final String ALGORITHM_NAME_MD5 = "MD5";

    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String md5(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM_NAME_MD5);
            byte[] result = digest.digest(data);
            return new String(encodeHex(result));
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(e);
        }
    }

    private static char[] encodeHex(byte[] bytes) {
        char[] chars = new char[32];
        for (int i = 0; i < chars.length; i = i + 2) {
            byte b = bytes[i / 2];
            chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
            chars[i + 1] = HEX_CHARS[b & 0xf];
        }
        return chars;
    }
}
