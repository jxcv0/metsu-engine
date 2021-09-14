package com.metsuengine;

import java.text.DecimalFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;

public class Metsu {
    public static void main( String[] args ) {

        CSVManager manager = new CSVManager("maptest.csv");

        TradeSeries tradeSeries = new TradeSeries();
        tradeSeries.setMaxSize(200);

        BybitWebSocket bybitWebSocket = new BybitWebSocket(tradeSeries);

        BybitEndpoint endpoint = new BybitEndpoint("BTCUSD");

        Thread websocketThread = new Thread(new BybitWebSocketClient(bybitWebSocket, "trade.BTCUSD"));
        websocketThread.start();

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {

            System.out.println(tradeSeries.calculateVWAP());

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }  
        }
    }
}