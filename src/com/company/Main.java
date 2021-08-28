package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        new Server((req, res)->{
            return "<html><body><h1>Hello bro!</h1><body></html>";
        }).bootstrap();
    }
}

