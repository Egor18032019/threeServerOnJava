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
    private final int PORT = 888;
    private final HttpHandler handler;
    private final static String HEADERS = "HTTP/1.1 200 OK \r\n" +
            "Server: threeServerOnJava\n" +
            "Content-Type: text/html\n" +
            "Content-Length: %s\n" +
            "Connection:close\n\n";

    public Server(HttpHandler handler) {
        this.handler = handler;
    }

    public void bootstrap() {
        try {
            server = AsynchronousServerSocketChannel.open();
            server.bind(new InetSocketAddress("127.0.0.1", 888));
            System.out.println("Server started on: " + PORT);
            while (true) {
                Future<AsynchronousSocketChannel> future = server.accept();
                System.out.println("Client connection");
                handleClient(future);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Future<AsynchronousSocketChannel> future) throws InterruptedException, ExecutionException, TimeoutException, IOException {
        AsynchronousSocketChannel clientChanel = future.get(55, TimeUnit.SECONDS);

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


            HttpRequest request = new HttpRequest(builder.toString());
            HttpResponse respons = new HttpResponse();
            String body = this.handler.handle(request, respons);

            String page = String.format(HEADERS, body.length()) + body;
            ByteBuffer resp = ByteBuffer.wrap(page.getBytes());
            clientChanel.write(resp);

            clientChanel.close();
        }
    }
}
