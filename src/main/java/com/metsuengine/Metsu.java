package com.metsuengine;

import java.text.DecimalFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Metsu {
    public static void main( String[] args ) {

        TradeSeries tradeSeries = new TradeSeries();
        tradeSeries.setMaxSize(20);

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
            System.out.println(ZonedDateTime.now(ZoneOffset.UTC) + " " +
                tradeSeries.getLastTrade().getPrice() + " " + 
                tradeSeries.calculateDelta() + " " +
                endpoint.getOrderBook().getTotalDepth() + " " +
                new DecimalFormat("###.##").format(endpoint.getOrderBook().getDeltaRatio()));
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}