package io.github.wuare.hl.util;

import top.wuare.json.Wson;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET_SALT = "waser";

    public static String generateToken(String userId, int expHours) {

        Wson wson = new Wson();

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "MD5");
        String header = Base64Util.encode(wson.toJson(headerMap));

        Map<String, String> payloadMap = new HashMap<>();
        Date now = new Date();
        payloadMap.put("iat", DateUtil.format(now, DateUtil.YYYY_MM_DD_HHmmss));
        payloadMap.put("exp", DateUtil.format(DateUtil.addHours(now, expHours), DateUtil.YYYY_MM_DD_HHmmss));
        payloadMap.put("userId", userId);
        String payload = Base64Util.encode(wson.toJson(payloadMap));

        String encodedStr = header + "." + payload;
        String signature = Md5Util.md5((encodedStr + SECRET_SALT).getBytes(StandardCharsets.UTF_8));
        return encodedStr + "." + signature;
    }
}
