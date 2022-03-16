package io.github.wuare.web.controller;

import io.github.wuare.web.anno.Controller;
import io.github.wuare.web.anno.GetMapping;
import io.github.wuare.web.anno.PostMapping;
import io.github.wuare.web.anno.ResponseBody;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;
import top.wuare.json.Wson;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
public class MsgController {

    public static final String msgPath = "msg.txt";
    public static final String msgLenPath = "msglen.txt";

    private final Wson wson = new Wson();

    @PostMapping("/msg/submit")
    public void submit(HttpRequest req) throws IOException {
        String body = req.getBody();
        Msg msg = wson.fromJson(body, Msg.class);
        File lenFile = new File(msgLenPath);
        int len = 0;
        if (lenFile.exists()) {
            List<String> lens = Files.readAllLines(Paths.get(msgLenPath));
            len = lens.isEmpty() ? 0 : Integer.parseInt(lens.get(0));
        }
        // 写留言msg.txt
        File file = new File(msgPath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            char[] nameChar = msg.getName().toCharArray();
            char[] textChar = msg.getText().toCharArray();
            writer.append(String.valueOf(nameChar.length));
            writer.newLine();
            writer.append(String.valueOf(textChar.length));
            writer.newLine();
            writer.append(CharBuffer.wrap(nameChar));
            writer.append(CharBuffer.wrap(textChar));
            writer.append('\n');
        }
        List<CharSequence> lenList = new ArrayList<>();
        lenList.add(CharBuffer.wrap(String.valueOf(len + 1).toCharArray()));
        Files.write(Paths.get(msgLenPath), lenList, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    @GetMapping("/msg/search")
    @ResponseBody
    public List<Msg> search() throws IOException {
        LinkedList<Msg> list = new LinkedList<>();
        File file = new File(msgPath);
        if (!file.exists()) {
            return list;
        }
        File lenFile = new File(msgLenPath);
        if (!lenFile.exists()) {
            return list;
        }
        List<String> lens = Files.readAllLines(Paths.get(msgLenPath));
        if (lens.isEmpty()) {
            return list;
        }
        int count = Integer.parseInt(lens.get(0));
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            for (int i = 0; i < count; i++) {
                int nameLen = Integer.parseInt(reader.readLine());
                int textLen = Integer.parseInt(reader.readLine());
                char[] nameBuf = new char[nameLen];
                int read = reader.read(nameBuf);
                if (nameLen != read) {
                    return new ArrayList<>();
                }
                char[] textBuf = new char[textLen];
                int read1 = reader.read(textBuf);
                if (textLen != read1) {
                    return new ArrayList<>();
                }
                reader.read();
                list.addFirst(new Msg(new String(nameBuf), new String(textBuf)));
            }

        }
        return list;
    }

    public static class Msg {
        public Msg() {
        }
        public Msg(String name, String text) {
            this.name = name;
            this.text = text;
        }

        private String name;
        private String text;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return "Msg{" +
                    "name='" + name + '\'' +
                    ", text='" + text + '\'' +
                    '}';
        }
    }
}
