package io.github.wuare.hl.filter;

import io.github.wuare.admin.domain.bo.token.JWT;
import io.github.wuare.admin.domain.common.ApiResponse;
import io.github.wuare.hl.anno.Filter;
import io.github.wuare.hl.exception.JwtDecodeException;
import io.github.wuare.hl.util.JsonUtil;
import io.github.wuare.hl.util.JwtUtil;
import io.github.wuare.hl.util.StringUtil;
import top.wuare.http.define.HttpStatus;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;
import top.wuare.http.util.HttpUtil;

import java.util.Date;

@Filter(order = 1)
public class AuthorizationWebFilter implements WebFilter {

    @Override
    public boolean doFilter(HttpRequest request, HttpResponse response) {

        // 白名单
        if (!"/api/role".equals(HttpUtil.getUrlWithOutQueryParam(request.getUrl()))) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (StringUtil.isEmpty(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED);
            return false;
        }
        try {
            JWT jwt = JwtUtil.decodeToken(token);
            Date now = new Date();
            if (now.getTime() > jwt.getExp()) {
                // 过期，需要刷新token
                response.setStatus(HttpStatus.UNAUTHORIZED);
                return false;
            }
        } catch (JwtDecodeException e) {
            response.setBody(JsonUtil.toJson(ApiResponse.error("身份认证失败")));
            return false;
        }
        return true;
    }

}
