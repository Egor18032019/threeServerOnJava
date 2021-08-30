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
        AsynchronousSocketChannel clientChanel = future.get(55, TimeUnit.MINUTES);
        new Thread(() -> {

        while (clientChanel != null && clientChanel.isOpen()) {
            System.out.println("Client acsepted");
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            StringBuilder builder = new StringBuilder();
            boolean keepReading = true;
            while (keepReading) {
                int readResult = 0; // прочитали
                try {
                    readResult = clientChanel.read(buffer).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println("Server 65 stroka");
                keepReading = readResult == BUFFER_SIZE;
                buffer.flip(); //вернулись в начало
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
                builder.append(charBuffer);
//                    buffer.flip(); // вернули курсор на позиицию
                buffer.clear();
            }

            HttpRequest request = new HttpRequest(builder.toString());
            HttpResponse respons = new HttpResponse();
            if (handler != null) {
                try {
                    String body = this.handler.handle(request, respons);
                    System.out.println("body");
                    if (body != null && !body.isBlank()) {
                        if (respons.getHeaders().get("Content-Type") == null) {
                            respons.addHeader("Content-Type", "text/html; charset=utf-8");
                        }
                         respons.setBody(body);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    respons.setStatusCode(500);
                    respons.setStatus("Internal server error");
                    respons.addHeader("Content-Type", "text/html; charset=utf-8");
                    respons.setBody("<html><body><h1>Errors happens <h1></body></html>");
                }
            } else {
                respons.setStatusCode(404);
                respons.setStatus("No found");
                respons.addHeader("Content-Type", "text/html; charset=utf-8");
                respons.setBody("<html><body><h1>Resourse not found<h1></body></html>");

            }
            ByteBuffer resp = ByteBuffer.wrap(respons.getBytes());
            clientChanel.write(resp);
            try {
                clientChanel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }).start();
    }
}
