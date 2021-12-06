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
        if (eval instanceof ReturnValue) {
            Object val = ((ReturnValue) eval).getVal();
            if (val == null) {
                response.setBody("null");
                return;
            }
            if (val instanceof BigDecimal) {
                response.setBody(((BigDecimal) val).toPlainString());
                return;
            }
            if (val instanceof Boolean) {
                response.setBody(((Boolean) val).toString());
                return;
            }
            response.setBody(val.toString());
            return;
        }
        response.setBody("执行完成，无返回值");
    }
}
