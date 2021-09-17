package io.github.wuare.hl;

import io.github.wuare.hl.anno.Controller;
import io.github.wuare.hl.anno.GetMapping;
import io.github.wuare.hl.anno.PostMapping;
import io.github.wuare.hl.util.ClassUtil;
import top.wuare.http.HttpServer;
import top.wuare.http.handler.RequestHandler;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class JavaHighLight {

    public void start() throws IOException {
        HttpServer httpServer = new HttpServer(80);
        List<String> allClass = ClassUtil.getAllClass();
        allClass.forEach(e -> {
            try {
                Class<?> aClass = Class.forName(e);
                Object ao = aClass.getDeclaredConstructor().newInstance();
                Annotation annotation = aClass.getAnnotation(Controller.class);
                if (annotation == null) {
                    return;
                }
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
        httpServer.start();
    }

    private RequestHandler genHandler(Object ao, Method m) {
        return (request, response) -> {
            setResponse(response);
            Class<?>[] parameterTypes = m.getParameterTypes();
            Object[] param = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> clazz = parameterTypes[i];
                param[i] = null;
                if (clazz.isAssignableFrom(HttpRequest.class)) {
                    param[i] = request;
                }
                if (clazz.isAssignableFrom(HttpResponse.class)) {
                    param[i] = response;
                }
            }
            try {
                m.invoke(ao, param);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    private void setResponse(HttpResponse response) {
        if (response == null) {
            return;
        }
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Expose-Headers", "Origin, Accept-Language, Accept-Encoding,X-Forwarded-For, Connection, Accept, User-Agent, Host, Referer,Cookie, Content-Type, Cache-Control");
    }

    public static void main(String[] args) throws Exception {
         JavaHighLight light = new JavaHighLight();
         light.start();
    }
}
