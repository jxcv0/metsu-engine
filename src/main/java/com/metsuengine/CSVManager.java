package com.metsuengine;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;

public class CSVManager {

    private Path path = null;

    public CSVManager(String file) {
        this.path = Paths.get(file);
    }

    public void writeLine(String[] line) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(this.path.toString(), true));
            writer.writeNext(line);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public BarSeries buildFromCSV() {

        InputStream stream = CSVManager.class.getClassLoader().getResourceAsStream(this.path.toString());

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
        
        BarSeries barSeries = new BaseBarSeries();

        for (String[] line : csvLines) {
            barSeries.addBar(
                ZonedDateTime.parse(line[0]),
                Double.parseDouble(line[1]),
                Double.parseDouble(line[2]),
                Double.parseDouble(line[3]),
                Double.parseDouble(line[4]),
                Double.parseDouble(line[5]));
        }
        return barSeries;
    }

    public void writeToCSV(BarSeries barSeries) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(this.path.toString(), true));
            List<Bar> bars = barSeries.getBarData();
            for (Bar bar : bars) {
                String[] candleData = {
                    bar.getEndTime().toString(),
                    bar.getOpenPrice().toString(),
                    bar.getHighPrice().toString(),
                    bar.getLowPrice().toString(),
                    bar.getClosePrice().toString(),
                    bar.getVolume().toString()
                };

                writer.writeNext(candleData);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToCSV(Bar bar, String file) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(this.path.toString(), true));
            String[] candleData = {
                bar.getEndTime().toString(),
                bar.getOpenPrice().toString(),
                bar.getHighPrice().toString(),
                bar.getLowPrice().toString(),
                bar.getClosePrice().toString(),
                bar.getVolume().toString()
            };

            writer.writeNext(candleData);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
