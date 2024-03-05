package com.company;

import com.company.schemas.Row;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class Reader {

    public static String readJsonAndGiveHtml() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("catalog.json"));
        String line = reader.lines().collect(Collectors.joining());

        ObjectMapper mapper = new ObjectMapper();
        Row[] rowArr = mapper.readValue(line, Row[].class);
        StringBuilder strForHtml = new StringBuilder();
        strForHtml.append("<table>");
        for (Row row : rowArr) {
            strForHtml.append("<tr>");
            strForHtml.append("<td>");
            strForHtml.append(row.getId());
            strForHtml.append("</td>");
            strForHtml.append("<td>");
            strForHtml.append(row.getCost());
            strForHtml.append("</td>");
            strForHtml.append("<td>");
            strForHtml.append(row.getName());
            strForHtml.append("</td>");
            strForHtml.append("<td>");
            strForHtml.append(row.getP1());
            strForHtml.append("</td>");
            strForHtml.append("<td>");
            strForHtml.append(row.getP2());
            strForHtml.append("</td>");
            strForHtml.append("<td>");
            strForHtml.append(row.getP3());
            strForHtml.append("</td>");
            strForHtml.append("<td>");
            strForHtml.append(row.getZalog());
            strForHtml.append("</td>");
            strForHtml.append("</tr>");
        }
        strForHtml.append("</table>");


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
                "           }\n" +
                "       table, th, td {\n" +
                "       border: 1px solid;\n" +
                "       }" +
                "    </style>\n" +
                "    <title>Табличка</title>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello, world!</h1>\n" +
                strForHtml +
                "</body>\n" +
                "</html>\n";
    }
}
