package io.github.wuare.hs;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServerTest {
    public void start(int port) throws IOException {
        ServerSocket ss = new ServerSocket(port);
        while (true) handle(ss.accept());
    }

    private void handle(Socket socket) throws IOException {
        try (OutputStream out = socket.getOutputStream()) {
            String text = "<h1>这是一个标题</h1><a href='https://github.com'>GitHub</a>";
            String res = "HTTP/1.1 200 OK\r\nContent-Type: text/html;charset=utf-8\r\nContent-Length: " + text.getBytes().length + "\r\n\r\n" + text;
            out.write(res.getBytes());
            out.flush();
        }
    }

    public static void main(String[] args) throws Exception {
        new HttpServerTest().start(8088);
    }
}
