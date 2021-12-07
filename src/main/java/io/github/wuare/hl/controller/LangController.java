package io.github.wuare.hl.controller;

import io.github.wuare.hl.anno.Controller;
import io.github.wuare.hl.anno.PostMapping;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;
import top.wuare.lang.interpreter.Interpreter;
import top.wuare.lang.type.ReturnValue;

import java.math.BigDecimal;

@Controller
public class LangController {

    @PostMapping("/lang/eval")
    public void eval(HttpRequest request, HttpResponse response) {
        String code = request.getBody();
        Interpreter interpreter = new Interpreter(code);
        Object eval = interpreter.eval();
        String consoleText = interpreter.getConsole().toString() + "<br/>";
        if (eval instanceof ReturnValue) {
            Object val = ((ReturnValue) eval).getVal();
            if (val == null) {
                response.setBody(consoleText + "null");
                return;
            }
            if (val instanceof BigDecimal) {
                response.setBody(consoleText + ((BigDecimal) val).toPlainString());
                return;
            }
            if (val instanceof Boolean) {
                response.setBody(consoleText + ((Boolean) val).toString());
                return;
            }
            response.setBody(consoleText + val.toString());
            return;
        }
        response.setBody(consoleText + "执行完成，无返回值");
    }
}
