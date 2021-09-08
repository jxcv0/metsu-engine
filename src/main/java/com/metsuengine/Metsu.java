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


        FrameSeries frameSeries = new FrameSeries("BTCUSD-Bybit", new ArrayList<Frame>());
        frameSeries.setMaxSize(10);
        
        while(true) {
            frameSeries.addFrame(new Frame(
                ZonedDateTime.now(ZoneOffset.UTC),
                BybitEndpoint.getLiquidations("BTCUSD", ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(1)),
                BybitEndpoint.getOrderBook("BTCUSD")
            ));

            System.out.println(frameSeries.getSeries().size());

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void createTestingData(FrameSeries frameSeries, int duration, int interval) {
        try {    
            for (int i = 0; i < duration; i++) {
                
                frameSeries.addFrame(new Frame(
                    ZonedDateTime.now(ZoneOffset.UTC),
                    BybitEndpoint.getLiquidations("BTCUSD", ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(1)),
                    BybitEndpoint.getOrderBook("BTCUSD")));
                
                System.out.println("getting data " + i + "/" + duration);
                
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