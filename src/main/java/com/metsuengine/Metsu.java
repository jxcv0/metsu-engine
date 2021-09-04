package com.metsuengine;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Metsu {
    public static void main( String[] args ){

        Snapshot snapshot = new Snapshot(
            BybitEndpoint.getLiquidations("BTCUSD", ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(1), "Buy"),
            BybitEndpoint.getOrderBook("BTCUSD"));

        CSVManager.writeLine("testing_data", snapshot.getTotalLiquidations("Buy") + snapshot.orderBook.getDeltaRatio());
        System.out.println(snapshot.getTotalLiquidations("Buy"));
        System.out.println(snapshot.orderBook.getDeltaRatio());
    }
}