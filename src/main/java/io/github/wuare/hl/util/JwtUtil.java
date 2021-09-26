package io.github.wuare.hl.util;

import io.github.wuare.admin.domain.bo.token.JWT;
import io.github.wuare.hl.exception.JwtDecodeException;
import top.wuare.json.Wson;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    public static final String BEARER_TEXT = "Bearer ";

    private static final String SECRET_SALT = "waser";

    public static String generateToken(String userId, int expMinutes) {

        Wson wson = new Wson();

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "MD5");
        String header = Base64Util.encode(wson.toJson(headerMap));

        Map<String, Object> payloadMap = new HashMap<>();
        Date now = new Date();
        payloadMap.put("iat", now.getTime());
        payloadMap.put("exp", DateUtil.addMinutes(now, expMinutes).getTime());
        payloadMap.put("userId", userId);
        String payload = Base64Util.encode(wson.toJson(payloadMap));

        String encodedStr = header + "." + payload;
        String signature = Md5Util.md5((encodedStr + SECRET_SALT).getBytes(StandardCharsets.UTF_8));
        return encodedStr + "." + signature;
    }

    public static JWT decodeToken(String token) throws JwtDecodeException {
        if (StringUtil.isEmpty(token)) {
            throw new JwtDecodeException("ERR_CODE_100, token is null");
        }
        if (token.startsWith(BEARER_TEXT)) {
            token = token.substring(BEARER_TEXT.length());
        }
        String[] split = token.split("\\.");
        if (split.length != 3) {
            throw new JwtDecodeException("ERR_CODE_200, token is invalid");
        }
        String encodedStr = split[0] + "." + split[1];
        String signature = Md5Util.md5((encodedStr + SECRET_SALT).getBytes(StandardCharsets.UTF_8));
        if (!signature.equals(split[2])) {
            throw new JwtDecodeException("ERR_CODE_300, signature error");
        }
        return JsonUtil.fromJson(Base64Util.decode(split[1]), JWT.class);
    }
}
