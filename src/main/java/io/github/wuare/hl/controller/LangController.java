package io.github.wuare.hl.controller;

import io.github.wuare.hl.anno.Controller;
import io.github.wuare.hl.anno.PostMapping;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;
import top.wuare.lang.interpreter.Interpreter;
import top.wuareb.highlight.gen.html.wa.WaGen;

@Controller
public class LangController {

    private final WaGen waGen = new WaGen();

    @PostMapping("/lang/eval")
    public void eval(HttpRequest request, HttpResponse response) {
        String code = request.getBody();
        Interpreter interpreter = new Interpreter(code);
        Object val = interpreter.eval();
        String consoleText = interpreter.getConsole().toString() + "<br/>";
        response.setBody(consoleText);
    }

    @PostMapping("/lang/hl")
    public void wa(HttpRequest request, HttpResponse response) {
        String text = request.getBody();
        response.setBody("");
        if (text != null) {
            response.setBody(waGen.gen(text));
        }
    }
}
