package com.metsuengine;

// import java.time.ZoneOffset;
// import java.time.ZonedDateTime;

public class Metsu {
    public static void main( String[] args ){
        
        BarSeries series = CSVManager.buildFromCSV("BTCUSDbybit", "testing_data.csv");

        Chart.buildTimeSeriesChart(series);
        Chart.buildRatioChart(series);
        Chart.buildDifferenceChart(series);

        // while (true) {

        //     Snapshot snapshot = new Snapshot(
        //         ZonedDateTime.now(ZoneOffset.UTC),
        //         BybitEndpoint.getLiquidations("BTCUSD", ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(1), "Buy"),
        //         BybitEndpoint.getOrderBook("BTCUSD"));

        //     String[] lines = {
        //         snapshot.getTime().toString(),
        //         Double.toString(snapshot.getOrderBook().getBestAsk()),
        //         Double.toString(snapshot.getOrderBook().getDepth("Buy")),
        //         Double.toString(snapshot.getOrderBook().getDepth("Sell")),
        //         Double.toString(snapshot.getOrderBook().getDeltaRatio())
        //     };

        //     CSVManager.writeLine("testing_data.csv", lines);
        //     try {
        //         Thread.sleep(1000);               
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        // }
    }
}