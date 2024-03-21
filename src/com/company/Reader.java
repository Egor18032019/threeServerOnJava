package com.company;

import com.company.schemas.Row;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.Date;

public class Reader {
    private static final String fileName = "catalog.json";

    public static String readJsonAndGiveHtml() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(fileName));

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

    public static String giveMeLastModifiedForHeader() {
        String lastModified = "";
        try {
            Path file = Paths.get(fileName);
            BasicFileAttributes attr =
                    Files.readAttributes(file, BasicFileAttributes.class);
            String someString = attr.lastModifiedTime().toString();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = format.parse(someString);   // получаем дату из строки
            lastModified = date.toString();
//            lastModified = format.format(date);
        } catch (IOException e) {
            System.out.println(e.getMessage() + "Не смогли получить дату.");
        } catch (ParseException e) {
            System.out.println(e.getMessage() + "Не смогли распарсить дату.");
        }
        return lastModified;
    }
}
