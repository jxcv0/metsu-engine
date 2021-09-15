package com.metsuengine;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;

public class Metsu {
    public static void main( String[] args ) {
        TradeSeries tradeSeries = new TradeSeries();

        ZonedDateTime tomorrow = ZonedDateTime.now(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).plusDays(1);
        
        BybitWebSocket bybitWebSocket = new BybitWebSocket(tradeSeries);

        Thread websocketThread = new Thread(new BybitWebSocketClient(bybitWebSocket, "trade.BTCUSD"));
        websocketThread.start();

        while (true) {
            if(ZonedDateTime.now(ZoneOffset.UTC).isBefore(tomorrow)) {
                System.out.println(tomorrow);
                double vwap = tradeSeries.calculateVWAP();
                // check for active position
                // check if price is above or below
                // if position is active
                // if above, chase with long position
                // if below chase with shot position              
            } else {
                // update tomorrow
                tradeSeries = new TradeSeries();
                tomorrow = ZonedDateTime.now(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).plusDays(1);
            }
        }
    }
}