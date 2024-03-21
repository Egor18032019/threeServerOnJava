package com.company;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Server {
    private AsynchronousServerSocketChannel server;
    private final static int BUFFER_SIZE = 256;
    private final int PORT = 8080;

    private static String HEADERS = "HTTP/1.1 200 OK \r\n" +
            "Server: threeServerOnJava\n" +
            "Content-Type: text/html\n" +
            "Content-Length: %s\n" +
            "Connection:close\n";


    public Server() {

    }

    public void bootstrap() {
        try {
            server = AsynchronousServerSocketChannel.open();
            server.bind(new InetSocketAddress("0.0.0.0", PORT));
            System.out.println("Server started on: " + PORT);
            while (true) {
                Future<AsynchronousSocketChannel> future = server.accept();
                System.out.println("Client connection");
                handleClient(future);
            }

        } catch (IOException | ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Future<AsynchronousSocketChannel> future) throws InterruptedException, ExecutionException, TimeoutException, IOException {
//        AsynchronousSocketChannel clientChanel = future.get(55, TimeUnit.SECONDS);
        AsynchronousSocketChannel clientChanel = future.get();

        while (clientChanel != null && clientChanel.isOpen()) {
            System.out.println("Client acsepted");
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            StringBuilder builder = new StringBuilder();
            boolean keepReading = true;
            while (keepReading) {
                int readResult = clientChanel.read(buffer).get(); // прочитали
                System.out.println("clientChanel.read(buffer).get()");
                keepReading = readResult == BUFFER_SIZE;
                buffer.flip(); //вернулись в начало
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
                builder.append(charBuffer);
//                    buffer.flip(); // вернули курсор на позиицию
                buffer.clear();
            }

            String html;
            try {
                html = Reader.readJsonAndGiveHtml();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String body = html;
// и добавляем в хедер дату модификация файла
            String lastModified ="Last-modified: "+ Reader.giveMeLastModifiedForHeader();
            HEADERS = HEADERS + lastModified + "\n\n";
            int length = body.getBytes().length;
            String page = String.format(HEADERS, length) + body;
            ByteBuffer resp = ByteBuffer.wrap(page.getBytes());
            clientChanel.write(resp);

            clientChanel.close();
        }
    }
}
