package com.company;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final String NEWLINE = "\r\n";
    private static final String HEADER_DELIMITER = "\r\n";
    private static final String DELIMITER = "\r\n\r\n";
    private final String message;
    private final String url;
    private final String body;
    private final HttpMetods method;
    private final Map<String, String> headers;

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public HttpMetods getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpRequest(String message) {
        this.message = message;
        String[] parts = message.split(DELIMITER);

        String head = parts[0];
        String[] headersArray = head.split(NEWLINE);
        String[] firstLine = headersArray[0].split(" ");
        method = HttpMetods.valueOf(firstLine[0]); // получили метод от клиента
        url = firstLine[1];
        this.headers = new HashMap<>();

        for (int i = 1; i < headersArray.length; i++) {
            String[] headerPart = headersArray[i].split(":", 2);
            headers.put(headerPart[0].trim(), headerPart[1].trim());

        }

        String bodyLength = this.headers.get("Content-Length");
        int length = bodyLength != null ? Integer.parseInt(bodyLength) : 0;
        this.body = parts.length > 1 ? parts[0].strip().substring(0, length) : " ";
    }
}
