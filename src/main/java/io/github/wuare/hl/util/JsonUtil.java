package io.github.wuare.hl.util;

import top.wuare.json.Wson;

public class JsonUtil {

    private static final Wson wson = new Wson();

    public static <T> T fromJson(String text, Class<T> t) {
        return wson.fromJson(text, t);
    }

    public static Object fromJson(String text) {
        return wson.fromJson(text);
    }

    public static String toJson(Object o) {
        return wson.toJson(o);
    }
}
