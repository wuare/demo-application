package io.github.wuare.hl;

import top.wuare.http.HttpServer;
import top.wuareb.highlight.gen.html.java.JavaGen;

public class JavaHighLight {

    private final JavaGen javaGen = new JavaGen();

    public void start() {
        HttpServer httpServer = new HttpServer(80);
        httpServer.post("/", ((request, response) -> {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Expose-Headers", "Origin, Accept-Language, Accept-Encoding,X-Forwarded-For, Connection, Accept, User-Agent, Host, Referer,Cookie, Content-Type, Cache-Control");
            String text = request.getBody();
            String genCode = javaGen.gen(text);
            response.setBody(genCode);
        }));
        httpServer.start();
    }

    public static void main(String[] args) {
        JavaHighLight light = new JavaHighLight();
        light.start();
    }
}
