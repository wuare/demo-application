package io.github.wuare.hl.interceptor;

import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;

import java.lang.reflect.Method;

public interface WebInterceptor {
    boolean preInvoke(HttpRequest request, HttpResponse response, Method method);
}
