package io.github.wuare.hl;

import io.github.wuare.hl.anno.*;
import io.github.wuare.hl.filter.WebFilter;
import io.github.wuare.hl.filter.WebFilterHolder;
import io.github.wuare.hl.util.ClassUtil;
import top.wuare.http.HttpServer;
import top.wuare.http.define.Constant;
import top.wuare.http.handler.RequestHandler;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;
import top.wuare.json.Wson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class JavaHighLight {

    private final Wson wson = new Wson();

    private final List<WebFilterHolder> filters = new ArrayList<>();

    public void start() throws IOException {
        HttpServer httpServer = new HttpServer(80);
        List<String> allClass = ClassUtil.getAllClass();
        allClass.forEach(e -> {
            try {
                Class<?> aClass = Class.forName(e);
                Filter filterAn = aClass.getAnnotation(Filter.class);
                if (filterAn != null && WebFilter.class.isAssignableFrom(aClass)) {
                    WebFilterHolder filterHolder = new WebFilterHolder(
                            (WebFilter) aClass.getDeclaredConstructor().newInstance(), filterAn.order());
                    filters.add(filterHolder);
                    return;
                }

                Annotation annotation = aClass.getAnnotation(Controller.class);
                if (annotation == null) {
                    return;
                }
                Object ao = aClass.getDeclaredConstructor().newInstance();
                Arrays.stream(aClass.getDeclaredMethods()).forEach(m -> {
                    GetMapping getAn = m.getAnnotation(GetMapping.class);
                    PostMapping postAn = m.getAnnotation(PostMapping.class);
                    if (getAn != null) {
                        httpServer.get(getAn.value(), genHandler(ao, m));
                    }
                    if (postAn != null) {
                        httpServer.post(postAn.value(), genHandler(ao, m));
                    }
                });
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
        filters.sort(Comparator.comparingInt(WebFilterHolder::getOrder));
        httpServer.start();
    }

    private RequestHandler genHandler(Object ao, Method m) {
        return (request, response) -> {
            for (WebFilterHolder filter : filters) {
                if (!filter.getWebFilter().doFilter(request, response)) {
                    return;
                }
            }
            Class<?>[] parameterTypes = m.getParameterTypes();
            Object[] param = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> clazz = parameterTypes[i];
                param[i] = null;
                if (clazz == HttpRequest.class) {
                    param[i] = request;
                }
                if (clazz == HttpResponse.class) {
                    param[i] = response;
                }
            }
            try {
                ResponseBody responseBody = m.getAnnotation(ResponseBody.class);
                if (responseBody != null) {
                    response.addHeader(Constant.HTTP_HEADER_CONTENT_TYPE, "application/json;charset=utf-8");
                    Object result = m.invoke(ao, param);
                    if (result != null) {
                        response.setBody(wson.toJson(result));
                    }
                } else {
                    m.invoke(ao, param);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public static void main(String[] args) throws Exception {
        JavaHighLight light = new JavaHighLight();
        light.start();
    }
}
