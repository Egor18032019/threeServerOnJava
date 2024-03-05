package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class Reader {

    public static String readJsonAndGiveHtml( ) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("file.json"));
        String line = reader.lines().collect(Collectors.joining());
        String[] correctLine = line.substring(1, line.length() - 1).split("}");
        StringBuilder str = new StringBuilder();
        str.append("<ul>");
        for (String point : correctLine) {
            String correctPoint = point.substring(0);
            str.append("<li>");
            str.append(correctPoint);
            str.append("</li>");
        }


        str.append("</ul>");

        reader.close();
         return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <style>\n" +
                "        H1 {\n" +
                "         font-size: 120%;\n" +
                "         font-family: Verdana, Arial, Helvetica, sans-serif;\n" +
                "         color: #333366;\n" +
                "        }\n" +
                "    </style>\n" +
                "    <title>Табличка</title>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello, world!</h1>\n" +
                str.toString() +
                "</body>\n" +
                "</html>\n";
    }
}
