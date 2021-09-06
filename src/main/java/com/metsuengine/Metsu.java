package com.metsuengine;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Metsu {
    public static void main( String[] args ){
        
        // FrameSeries series = CSVManager.buildFromCSV("BTCUSDbybit", "testing_data.csv");

        // Chart.buildTimeSeriesChart(series);
        // Chart.buildRatioChart(series);
        // Chart.buildDifferenceChart(series);

        while (true) {

            try {
                FileOutputStream fileOutputStream = new FileOutputStream("testfile.txt", true);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                objectOutputStream.writeObject(new Frame(
                    ZonedDateTime.now(ZoneOffset.UTC),
                    BybitEndpoint.getLiquidations("BTCUSD", ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(1)),
                    BybitEndpoint.getOrderBook("BTCUSD")));

                fileOutputStream.close();

                Thread.sleep(1000);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            // Person p2 = (Person) objectInputStream.readObject();
            // objectInputStream.close(); 
        }
    }
}