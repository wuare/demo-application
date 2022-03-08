package io.github.wuare.admin.interceptor;

import io.github.wuare.admin.anno.Permission;
import io.github.wuare.admin.domain.bo.token.JWT;
import io.github.wuare.admin.domain.common.ApiResponse;
import io.github.wuare.web.anno.Interceptor;
import io.github.wuare.web.exception.JwtDecodeException;
import io.github.wuare.web.interceptor.WebInterceptor;
import io.github.wuare.web.util.JsonUtil;
import io.github.wuare.web.util.JwtUtil;
import io.github.wuare.web.util.StringUtil;
import top.wuare.http.define.HttpStatus;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;

import java.lang.reflect.Method;

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
            if (JwtUtil.isExpire(jwt.getExp())) {
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
