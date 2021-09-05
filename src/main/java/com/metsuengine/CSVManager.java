package com.metsuengine;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVManager {

    public static void writeLine(String fileName, String[] lines) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName, true));
            writer.writeNext(lines);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public static BarSeries buildFromCSV(String name, String fileName) {

        InputStream stream = CSVManager.class.getClassLoader().getResourceAsStream(fileName);

        CSVReader reader = null;
        List<String[]> csvLines = null;

        try {
            reader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            csvLines = reader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        BarSeries series = build(name, csvLines);
        return series;
    }

    public static BarSeries build(String name, List<String[]> csvLines) {

        BarSeries series = new BarSeries();
        series.setName(name);

        for (int i = 0; i < csvLines.size(); i++) {
            String[] line = csvLines.get(i);

            Bar bar = new Bar(
                ZonedDateTime.parse(line[0]),
                Double.parseDouble(line[1]),
                Double.parseDouble(line[2]),
                Double.parseDouble(line[3]),
                Double.parseDouble(line[4])                
            );

            series.addBar(bar);
        }

        return series;
    }
}
