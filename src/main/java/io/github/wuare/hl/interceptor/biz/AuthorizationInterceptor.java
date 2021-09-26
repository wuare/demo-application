package io.github.wuare.hl.interceptor.biz;

import io.github.wuare.admin.domain.bo.token.JWT;
import io.github.wuare.admin.domain.common.ApiResponse;
import io.github.wuare.hl.anno.Interceptor;
import io.github.wuare.hl.anno.biz.Permission;
import io.github.wuare.hl.exception.JwtDecodeException;
import io.github.wuare.hl.interceptor.WebInterceptor;
import io.github.wuare.hl.util.JsonUtil;
import io.github.wuare.hl.util.JwtUtil;
import io.github.wuare.hl.util.StringUtil;
import top.wuare.http.define.HttpStatus;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;

import java.lang.reflect.Method;
import java.util.Date;

@Interceptor
public class AuthorizationInterceptor implements WebInterceptor {

    @Override
    public boolean preInvoke(HttpRequest request, HttpResponse response, Method method) {

        Permission permissionAn = method.getAnnotation(Permission.class);
        if (permissionAn == null) {
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
