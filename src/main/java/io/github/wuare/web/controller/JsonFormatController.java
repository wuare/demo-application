package io.github.wuare.web.controller;

import io.github.wuare.web.anno.Controller;
import io.github.wuare.web.anno.PostMapping;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;
import top.wuare.json.Wson;
import top.wuare.json.exception.CommonException;
import top.wuareb.highlight.gen.Gen;
import top.wuareb.highlight.gen.html.java.JavaGen;
import top.wuareb.highlight.gen.html.json.JsonGen;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
@Controller
public class JsonFormatController {

    private final Wson wson = new Wson();
    private final Gen javaGen = new JavaGen();

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

    @SuppressWarnings("unchecked")
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
            return "<span class='hl-bol'>" + o + "</span>";
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

    @PostMapping("/json/hl")
    public void hl(HttpRequest request, HttpResponse response) {
        String text = request.getBody();
        Gen gen = new JsonGen();
        String r = gen.gen(text);
        response.setBody(r);
    }

    @PostMapping("/json/code/java")
    public void codeJava(HttpRequest request, HttpResponse response) {
        String text = request.getBody();
        Object o;
        try {
            o = wson.fromJson(text);
        } catch (CommonException e) {
            response.setBody("JSON格式错误：" + e.getMessage());
            return;
        }
        AtomicInteger aIn = new AtomicInteger();
        response.setBody(javaGen.gen(doCodeJava(o, aIn, "ClazzName")));
    }

    private String doCodeJava(Object obj, AtomicInteger aIn, String clazzName) {
        boolean isMap = obj instanceof Map;
        if (!isMap) {
            return "";
        }
        StringBuilder c = new StringBuilder("public class " + clazzName + " {\n");
        String tab = "\t";
        List<Object> list = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) obj;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String fieldName = getFieldType(entry.getValue());

            if (entry.getValue() instanceof Map) {
                fieldName += aIn.incrementAndGet();
                list.add(entry.getValue());
                nameList.add(fieldName);
            }
            if (entry.getValue() instanceof List) {
                List<?> vl = (List<?>) entry.getValue();
                if (!vl.isEmpty()) {
                    if (vl.get(0) instanceof Map) {
                        String fieldClazzName = "ClazzName" + aIn.incrementAndGet();
                        fieldName += "<" + fieldClazzName + ">";
                        list.add(vl.get(0));
                        nameList.add(fieldClazzName);
                    } else {
                        fieldName += "<" + getFieldType(vl.get(0)) + ">";
                    }
                }
            }
            c.append(tab)
                    .append("private ")
                    .append(fieldName)
                    .append(" ")
                    .append(entry.getKey())
                    .append(";")
                    .append("\n");
        }
        c.append("}").append("\n\n");

        for (int i = 0; i < list.size(); i++) {
            c.append(doCodeJava(list.get(i), aIn, nameList.get(i)));
        }
        return c.toString();
    }

    private String getFieldType(Object obj) {
        if (obj == null) {
            return "Object";
        }
        if (obj instanceof BigDecimal) {
            return "BigDecimal";
        }
        if (obj instanceof String) {
            return "String";
        }
        if (obj instanceof Boolean) {
            return "Boolean";
        }
        if (obj instanceof Map) {
            return "ClazzName";
        }
        if (obj instanceof List) {
            return "List";
        }
        return "UnKnow";
    }
}
