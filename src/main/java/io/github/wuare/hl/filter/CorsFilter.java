package io.github.wuare.hl.filter;

import io.github.wuare.hl.anno.Filter;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;

@Filter
public class CorsFilter implements WebFilter {

    @Override
    public boolean doFilter(HttpRequest request, HttpResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Expose-Headers", "Origin, Accept-Language, Accept-Encoding,X-Forwarded-For, Connection, Accept, User-Agent, Host, Referer,Cookie, Content-Type, Cache-Control");
        return true;
    }
}
