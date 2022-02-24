package io.github.wuare.ot;

import java.util.logging.Logger;

/**
 * @author wuare
 */
public class JavaHighlightTest {

    private static final Logger logger = Logger.getLogger(JavaHighlightTest.class.getName());

    public int num(int a, int b) {
        return a + b;
    }

    public int number() {
        return 1000;
    }

    public String str() {
        return "高亮测试";
    }

    @Deprecated
    public void anno() {
    }

    public void test() {
        logger.info("test...");
    }
}
