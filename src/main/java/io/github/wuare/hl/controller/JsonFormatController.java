package io.github.wuare.hl.controller;

import io.github.wuare.hl.anno.Controller;
import io.github.wuare.hl.anno.PostMapping;
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
            response.setBody("<pre style=\"color: #F3592A; padding: 20px 20px;\">JSON格式错误</pre>");
            return;
        }
        String s = doFormat(o, "");
        String builder = "<pre style=\"background-color: #2B2B2B; color: white; padding: 20px 20px;\">\n" +
                s +
                "\n</pre>\n";
        response.setBody(builder);
    }

    private String doFormat(Object o, String pad) {
        if (o == null) {
            return "<span style='color: #F3592A;'>" + "null" + "</span>";
        }
        if (o instanceof BigDecimal) {
            return "<span style='color: #5596BA;'>" + ((BigDecimal) o).toPlainString() + "</span>";
        }
        if (o instanceof String) {
            return "<span style='color: #698652;'>" + "\"" + (String) o + "\"" + "</span>";
        }
        if (o instanceof Boolean) {
            return "<span style='color: #097BED;'>" +  ((Boolean) o).toString() +  "</span>";
        }
        if (o instanceof Map) {
            StringBuilder s = new StringBuilder();
            s.append("{").append("\n");
            Map<String, Object> map = (Map<String, Object>) o;
            String oldPad = pad;
            pad = pad + "    ";
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                s.append(pad).append("<span style='color: #BA5F60;'>")
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
