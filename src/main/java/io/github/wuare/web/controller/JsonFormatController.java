package io.github.wuare.web.controller;

import io.github.wuare.web.anno.Controller;
import io.github.wuare.web.anno.PostMapping;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;
import top.wuare.json.Wson;
import top.wuare.json.exception.CommonException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
public class JsonFormatController {

    private final Wson wson = new Wson();

    @PostMapping("/json/format")
    public void format(HttpRequest request, HttpResponse response) {
        String text = request.getBody();
        Object o;
        try {
            o = wson.fromJson(text);
        } catch (CommonException e) {
            System.out.println(e.getMessage());
            response.setBody("JSON格式错误：" + e.getMessage());
            return;
        }
        String s = doFormat(o, "");
        response.setBody(s);
    }

    private String doFormat(Object o, String pad) {
        if (o == null) {
            return "<span class='hl-nil'>" + "null" + "</span>";
        }
        if (o instanceof BigDecimal) {
            return "<span class='hl-num'>" + ((BigDecimal) o).toPlainString() + "</span>";
        }
        if (o instanceof String) {
            return "<span class='hl-str'>" + "\"" + o + "\"" + "</span>";
        }
        if (o instanceof Boolean) {
            return "<span class='hl-bol'>" + ((Boolean) o).toString() + "</span>";
        }
        if (o instanceof Map) {
            StringBuilder s = new StringBuilder();
            s.append("{").append("\n");
            Map<String, Object> map = (Map<String, Object>) o;
            String oldPad = pad;
            pad = pad + "    ";
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                s.append(pad).append("<span class='hl-key'>")
                        .append("\"").append(entry.getKey()).append("\"")
                        .append("</span>")
                        .append(": ")
                        .append(doFormat(entry.getValue(), pad)).append(",").append("\n");
            }
            if (s.length() > 2) {
                s.setLength(s.length() - 2);
            }
            s.append("\n").append(oldPad).append("}");
            return s.toString();
        }
        if (o instanceof List) {
            StringBuilder s = new StringBuilder();
            s.append("[").append("\n");
            List<Object> list = (List<Object>) o;
            String oldPad = pad;
            pad = pad + "    ";
            for (Object obj : list) {
                s.append(pad).append(doFormat(obj, pad)).append(",").append("\n");
            }
            if (s.length() > 2) {
                s.setLength(s.length() - 2);
            }
            s.append("\n").append(oldPad).append("]");
            return s.toString();
        }
        return null;
    }
}
