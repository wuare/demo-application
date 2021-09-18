package io.github.wuare.hl.controller;


import io.github.wuare.hl.anno.Controller;
import io.github.wuare.hl.anno.GetMapping;
import io.github.wuare.hl.anno.PostMapping;
import io.github.wuare.hl.util.DbUtil;
import top.wuare.http.define.HttpStatus;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpRequestLine;
import top.wuare.http.proto.HttpResponse;
import top.wuareb.highlight.gen.html.java.JavaGen;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

@Controller
public class LightController {

    private final JavaGen javaGen = new JavaGen();

    @GetMapping("/test")
    public void test(HttpRequest request, HttpResponse response) {
        List<Map<String, Object>> query = DbUtil.query("select * from user");
        response.setBody(query.toString());
    }

    @GetMapping("/a")
    public void a(HttpRequest request, HttpResponse response) {
        response.setBody("a");
    }

    @PostMapping("/")
    public void index(HttpRequest request, HttpResponse response) {
        String text = request.getBody();
        String genCode = javaGen.gen(text);
        response.setBody(genCode);
    }

    @GetMapping("/")
    public void home(HttpRequest request, HttpResponse response) {
        response.setStatus(302, "");
        response.addHeader("Location", "/home.html");
    }

    @GetMapping("/dl")
    public void dl(HttpRequest request, HttpResponse response) throws IOException {
        String name = ((HttpRequestLine) request.getHttpMessage().getHttpLine()).getQueryParam();
        URL url = LightController.class.getClassLoader().getResource(name);
        if (url == null) {
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setBody("<h1>404 Not Found</h1>");
            return;
        }
        response.addHeader("Content-Type", "application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=1.jpg");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Expires", "0");
        URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int c;
        while ((c = in.read(buf)) != -1) {
            out.write(buf, 0, c);
        }
        response.setBody(out.toByteArray());
    }
}
