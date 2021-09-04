package com.metsuengine;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Metsu {
    public static void main( String[] args ){

        Snapshot snapshot = new Snapshot(
            ZonedDateTime.now(ZoneOffset.UTC),
            BybitEndpoint.getLiquidations("BTCUSD", ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(1), "Buy"),
            BybitEndpoint.getOrderBook("BTCUSD"));

        String[] lines = {
            Double.toString(snapshot.getTotalLiquidations("Buy")),
            Double.toString(snapshot.getOrderBook().getDeltaRatio())
        };

        CSVManager.writeLine("testing_data.csv", lines);

        System.out.println(snapshot.getTotalLiquidations("Buy"));
        System.out.println(snapshot.getOrderBook().getDeltaRatio());
    }
}