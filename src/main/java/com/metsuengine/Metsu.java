package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {

        TradeSeries tradeSeries = new TradeSeries();
        tradeSeries.setMaxSize(5);

        BybitWebSocket bybitWebSocket = new BybitWebSocket(tradeSeries);

        Thread websocketThread = new Thread(new BybitWebSocketClient(bybitWebSocket, "trade.BTCUSD"));
        websocketThread.start();

        // TODO consumer for tradeSeries?

        while (true) {
            System.out.println(tradeSeries.calculateDelta());
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}