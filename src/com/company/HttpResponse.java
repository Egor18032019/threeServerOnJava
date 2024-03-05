package com.company;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final String NEWLINE = "\r\n";
    private Map<String, String> headers;
    private int statusCode = 200;
    private String status = "OK";
    private String body = "";

    public HttpResponse() {
        this.headers = new HashMap<>();
        this.headers.put("Server","threeServerOnJava");
        this.headers.put("Connection","Close");
    }

    public void addHeader (String key, String value){
        this.headers.put(key,value);
    }
    public void addHeaders (Map<String,String> headers){
        this.headers.putAll(headers);
    }

    public String message (){
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 ")
                .append(statusCode)
                .append(" ")
                .append(status)
                .append(NEWLINE);
        for (Map.Entry<String,String> entry : headers.entrySet()){
            System.out.println(entry);
            System.out.println(headers.entrySet());
            builder.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(NEWLINE);
        }
        return builder
                .append(NEWLINE)
                .append(body)
                .toString();
    }

    public byte[] getBytes (){
        return  message().getBytes();
    };



    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBody(String body) {
        this.headers.put("Content-Length", String.valueOf(body.length()));
        this.body = body;
    }

    public static String getNEWLINE() {
        return NEWLINE;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
