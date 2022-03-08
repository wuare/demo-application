package io.github.wuare.web.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {

    public static String encode(String src) {
        if (src == null) {
            return null;
        }
        return new String(encode(src.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public static String decode(String src) {
        if (src == null) {
            return null;
        }
        return new String(decode(src.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public static byte[] encode(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getEncoder().encode(src);
    }

    public static byte[] decode(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getDecoder().decode(src);
    }
}
