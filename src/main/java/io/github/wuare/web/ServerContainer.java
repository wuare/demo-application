package io.github.wuare.web;

import io.github.wuare.web.anno.*;
import io.github.wuare.web.filter.WebFilter;
import io.github.wuare.web.filter.WebFilterHolder;
import io.github.wuare.web.interceptor.WebInterceptor;
import io.github.wuare.web.interceptor.WebInterceptorHolder;
import io.github.wuare.web.mixin.DocHolder;
import io.github.wuare.web.util.ClassUtil;
import top.wuare.http.HttpServer;
import top.wuare.http.define.Constant;
import top.wuare.http.handler.RequestHandler;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;
import top.wuare.json.Wson;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerContainer {

    private static final Logger logger = Logger.getLogger(ServerContainer.class.getName());

    public static final ServerContainer instance = new ServerContainer();

    public static final HttpServer httpServer = new HttpServer(8001);

    private final Wson wson = new Wson();
    private final List<WebFilterHolder> filters = new ArrayList<>();
    private final List<WebInterceptorHolder> interceptorHolders = new ArrayList<>();
    private final List<DocHolder> docHolders = new ArrayList<>();
    public List<DocHolder> getDocHolders() {
        return docHolders;
    }

    public void start() throws IOException {
        scanAllClass();
        filters.sort(Comparator.comparingInt(WebFilterHolder::getOrder));
        interceptorHolders.sort(Comparator.comparingInt(WebInterceptorHolder::getOrder));
        httpServer.setAfterInitConsumer(afterInitConsumer());
        httpServer.start();
    }

    private void scanAllClass() throws IOException {
        List<String> allClass = ClassUtil.getAllClass();
        allClass.forEach(e -> {
            try {
                Class<?> aClass = Class.forName(e);

                if (aClass.isAnnotation() || aClass.isInterface() || aClass.isEnum()) {
                    return;
                }

                Filter filterAn = aClass.getAnnotation(Filter.class);
                if (filterAn != null && WebFilter.class.isAssignableFrom(aClass)) {
                    WebFilterHolder filterHolder = new WebFilterHolder(
                            (WebFilter) aClass.getDeclaredConstructor().newInstance(), filterAn.order());
                    filters.add(filterHolder);
                    return;
                }

                Interceptor interceptorAn = aClass.getAnnotation(Interceptor.class);
                if (interceptorAn != null && WebInterceptor.class.isAssignableFrom(aClass)) {
                    WebInterceptorHolder interceptorHolder = new WebInterceptorHolder();
                    interceptorHolder.setWebInterceptor((WebInterceptor) aClass.getDeclaredConstructor().newInstance());
                    interceptorHolder.setOrder(interceptorAn.order());
                    interceptorHolders.add(interceptorHolder);
                    return;
                }

                Controller controllerAn = aClass.getAnnotation(Controller.class);
                if (controllerAn != null) {
                    Object ao = aClass.getDeclaredConstructor().newInstance();
                    Arrays.stream(aClass.getDeclaredMethods()).forEach(m -> {
                        GetMapping getAn = m.getAnnotation(GetMapping.class);
                        PostMapping postAn = m.getAnnotation(PostMapping.class);
                        if (getAn != null) {
                            httpServer.get(getAn.value(), genHandler(ao, m));
                            docHolders.add(new DocHolder(getAn.value(), "GET"));
                        }
                        if (postAn != null) {
                            httpServer.post(postAn.value(), genHandler(ao, m));
                            docHolders.add(new DocHolder(postAn.value(), "POST"));
                        }
                    });
                }
            } catch (Exception ex) {
                logger.log(Level.WARNING, "initialization error, " + ex.getMessage());
            }
        });
    }

    private Consumer<HttpServer> afterInitConsumer() {
        String proto = ClassUtil.getProtocol();
        return t -> {
            if ("file".equals(proto)) {
                t.getConfig().setStaticResourcePathType("classpath");
                logger.info("检测到使用IDE运行项目，静态资源加载方式配置为classpath");
            }
        };
    }

    private RequestHandler genHandler(Object ao, Method m) {
        return (request, response) -> {
            for (WebFilterHolder filter : filters) {
                if (!filter.getWebFilter().doFilter(request, response)) {
                    return;
                }
            }
            for (WebInterceptorHolder holder : interceptorHolders) {
                if (!holder.getWebInterceptor().preInvoke(request, response, m)) {
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
}
