package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        new Server((req, res)->{
            return "<html><body><h1>Hello bro!</h1><h2>wow 222</h2></body></html>";
        }).bootstrap();
    }
}

