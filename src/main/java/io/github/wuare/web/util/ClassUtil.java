package io.github.wuare.web.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ClassUtil {

    public static List<String> getAllClass() throws IOException {
        List<String> list = new ArrayList<>();
        URL url = ClassUtil.class.getResource("");
        if (url == null) {
            return list;

        }
        String proto = url.getProtocol();
        if ("jar".equals(proto)) {
            JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName();
                if (name.endsWith(".class")) {
                    list.add(name.replaceAll("/", ".").substring(0, name.length() - 6));
                }
            }
        } else if ("file".equals(proto)) {
            URL u = ClassUtil.class.getClassLoader().getResource("");
            if (u == null) {
                return list;
            }
            String path = URLDecoder.decode(u.getPath(), "UTF-8");
            List<String> nameList = FileUtil.allFile(path);
            List<String> clazzNameList = nameList.stream().filter(e -> e.endsWith(".class"))
                    .map(e -> e.substring(path.length() - 1))
                    .map(e -> {
                        String s = replaceSeparatorToDot(e);
                        return s.substring(0, s.length() - 6);
                    }).collect(Collectors.toList());
            list.addAll(clazzNameList);
        }
        return list;
    }

    public static String replaceSeparatorToDot(String path) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == File.separatorChar) {
                builder.append(".");
            } else {
                builder.append(path.charAt(i));
            }
        }
        return builder.toString();
    }
}
