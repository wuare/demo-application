package io.github.wuare.hl.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FileUtil {

    public static List<String> allFile(String path) {
        List<String> list = new ArrayList<>();
        if (StringUtil.isEmpty(path)) {
            return list;
        }
        File file = new File(path);
        if (!file.exists()) {
            return list;
        }
        if (file.isFile()) {
            list.add(file.getPath());
            return list;
        }
        if (file.isDirectory()) {
            Arrays.stream(Optional.ofNullable(file.listFiles()).orElse(new File[0])).forEach(e -> {
                list.addAll(allFile(e.getPath()));
            });
        }
        return list;
    }
}
