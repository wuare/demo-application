package io.github.wuare.hl.controller;

import io.github.wuare.hl.anno.Controller;
import io.github.wuare.hl.anno.PostMapping;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;
import top.wuareb.highlight.gen.html.markdown.MdGen;

@Controller
public class MdController {

    private final MdGen mdGen = new MdGen();

    @PostMapping("/md/render")
    public void render(HttpRequest request, HttpResponse response) {
        String text = request.getBody();
        response.setBody("");
        if (text != null) {
            response.setBody(mdGen.gen(text));
        }
    }
}
