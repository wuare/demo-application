package io.github.wuare.web.util;

public class IOUtil {

    public static void close(AutoCloseable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception ignored) {
        }
    }
}
