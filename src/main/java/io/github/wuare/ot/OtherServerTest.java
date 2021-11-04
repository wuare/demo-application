package io.github.wuare.ot;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class OtherServerTest {

    private final int port;

    public OtherServerTest(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocket ss = new ServerSocket(port);
        System.out.println(ss.getReceiveBufferSize());
        Socket socket = ss.accept();
        InputStream inputStream = socket.getInputStream();
        byte[] buf = new byte[4096];
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        for (;;) {
            try {
                Thread.sleep(2000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int read = inputStream.read(buf);
            System.out.println("read buf length: " + read);
            byteBuffer.put(buf[0]);
            byteBuffer.put(buf[1]);
            byteBuffer.put(buf[2]);
            byteBuffer.put(buf[3]);
            byteBuffer.flip();
            int anInt = byteBuffer.getInt();
            byteBuffer.clear();
            System.out.println("read content: " + anInt);
        }
    }

    public static void main(String[] args) throws IOException {
        new OtherServerTest(8088).start();
    }
}
