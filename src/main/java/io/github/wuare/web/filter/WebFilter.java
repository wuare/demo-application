package io.github.wuare.web.filter;

import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;

public interface WebFilter {
    boolean doFilter(HttpRequest request, HttpResponse response);
}
