package com.metsuengine;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Metsu {
    public static void main( String[] args ){
        
        BarSeries series = CSVManager.buildFromCSV("BTCUSDbybit", "testing_data.csv");
        System.out.println(series.getBar(0).getDeltaRatio());

        // while (true) {

            // Snapshot snapshot = new Snapshot(
            //     ZonedDateTime.now(ZoneOffset.UTC),
            //     BybitEndpoint.getLiquidations("BTCUSD", ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(1), "Buy"),
            //     BybitEndpoint.getOrderBook("BTCUSD"));

            // String[] lines = {
            //     snapshot.getTime().toString(),
            //     Double.toString(snapshot.getTotalLiquidations("Buy")),
            //     Double.toString(snapshot.getOrderBook().getDeltaRatio()),
            //     Double.toString(snapshot.getOrderBook().getBestAsk())
            // };

            // CSVManager.writeLine("testing_data.csv", lines);
            // try {
            //     Thread.sleep(1000);               
            // } catch (Exception e) {
            //     e.printStackTrace();
            // }
        // }
    }
}