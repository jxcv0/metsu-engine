package com.metsuengine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Metsu {
    public static void main( String[] args ){

        FrameSeries frameSeries = loadFrameSeries("BTCUSD-Bybit.txt");
        
        Chart.buildTimeSeriesChart(frameSeries);
        Chart.buildRatioChart(frameSeries);
        Chart.buildDifferenceChart(frameSeries);

        // FrameSeries frameSeries = new FrameSeries("BTCUSD-Bybit", new ArrayList<Frame>());
        // createTestingData(frameSeries, 1000, 1000);

    }

    public static void createTestingData(FrameSeries frameSeries, int duration, int interval) {
        try {    
            for (int i = 0; i < duration; i++) {
                
                frameSeries.addFrame(new Frame(ZonedDateTime.now(ZoneOffset.UTC),
                    BybitEndpoint.getLiquidations("BTCUSD", ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(1)),
                    BybitEndpoint.getOrderBook("BTCUSD")));
                
                Thread.sleep(interval);
                
            }

        
            FileOutputStream fileOutputStream = new FileOutputStream("BTCUSD-Bybit.txt", true);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(frameSeries);

            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FrameSeries loadFrameSeries(String file) {
        try {
            FileInputStream  fileInputStream = new FileInputStream("BTCUSD-Bybit.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            
            FrameSeries frameSeries = (FrameSeries) objectInputStream.readObject();
            objectInputStream.close();

            return frameSeries;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}