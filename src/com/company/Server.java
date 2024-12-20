package com.company;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public class Server {
    private AsynchronousServerSocketChannel server;
    private final static int BUFFER_SIZE = 256;
    private final int PORT = 8080;

    private final static String HEADERS = "HTTP/1.1 200 OK \r\n" +
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
                keepReading = readResult == BUFFER_SIZE;
                buffer.flip(); //вернулись в начало
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
                builder.append(charBuffer);
//                    buffer.flip(); // вернули курсор на позиицию
                buffer.clear();

            }

            String request = builder.toString();
            System.out.println(request);
            Map<String, String> queryParams = extractQueryParameters(request);
            int sum = calculateSum(queryParams);


            String body =  String.valueOf(sum);
// и добавляем в хедер дату модификация файла
            String lastModified = "Last-modified: " + new java.util.Date().toString();
            String headerForThis = HEADERS + lastModified + "\n\n";
            System.out.println(headerForThis);
            int length = body.getBytes().length;
            String page = String.format(headerForThis, length) + body;
            ByteBuffer resp = ByteBuffer.wrap(page.getBytes());
            clientChanel.write(resp);

            clientChanel.close();
        }
    }

    private int calculateSum(Map<String, String> queryParams) {
        int sum = 0;
        for (String value : queryParams.values()) {
            sum += Integer.parseInt(value);
        }
        return sum;
    }

    private Map<String, String> extractQueryParameters(String request) {
        //GET /?a=2&b=3 HTTP/1.1
        Map<String, String> params = new HashMap<>();
        String[] lines = request.split("\r\n");
        for (String line : lines) {
            if (line.startsWith("GET")) {
                String[] parts = line.split("\\?");
//                a=2&b=3 HTTP/1.1
                if (parts.length > 1) {
                    String queryString = parts[1];
                    String[] keyValuePairs = queryString.split("&");
//                    a=2, b=3  HTTP/1.1
                    for (String pair : keyValuePairs) {
                        String[] kv = pair.split("=");
//                        b , 3  HTTP/1.1
                        if (kv.length == 2) {
                            params.put(kv[0], kv[1].split(" ")[0]);
                        }
                    }
                }
            }
        }
        return params;
    }

}
