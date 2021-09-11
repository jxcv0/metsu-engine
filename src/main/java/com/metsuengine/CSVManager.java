package com.metsuengine;

import java.io.FileWriter;
import java.util.List;

import com.opencsv.CSVWriter;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;

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

    // public static FrameSeries buildFromCSV(String name, String fileName) {

    //     InputStream stream = CSVManager.class.getClassLoader().getResourceAsStream(fileName);

    //     CSVReader reader = null;
    //     List<String[]> csvLines = null;

    //     try {
    //         reader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
    //         csvLines = reader.readAll();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     } finally {
    //         if (reader != null) {
    //             try {
    //                 reader.close();
    //             } catch (IOException e) {
    //                 e.printStackTrace();
    //             }
    //         }
    //     }
        
    //     FrameSeries series = build(name, csvLines);
    //     return series;
    // }

    public static void writeToCSV(BarSeries barSeries) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter("kline.csv", true));
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

    public static void writeToCSV(Bar bar, String file) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(file, true));
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

    public static BarSeries loadFromCSV(String file) {
        // TODO
        return null;
    }
}
