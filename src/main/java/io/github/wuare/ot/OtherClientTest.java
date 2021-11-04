package io.github.wuare.ot;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class OtherClientTest {

    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 8088);
        System.out.println(s.getSendBufferSize());
        OutputStream outputStream = s.getOutputStream();
        byte[] buf = new byte[4096];
        int i = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        for (;;) {
            i++;
            byteBuffer.putInt(i);
            byte[] array = byteBuffer.array();
            byteBuffer.clear();
            buf[0] = array[0];
            buf[1] = array[1];
            buf[2] = array[2];
            buf[3] = array[3];
            outputStream.write(buf);
            outputStream.flush();
            Thread.sleep(10);
            System.out.println("send content: " + i);
        }
    }
}
