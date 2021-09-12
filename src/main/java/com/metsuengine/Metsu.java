package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {

        TradeSeries tradeSeries = new TradeSeries();
        BybitWebSocket bybitWebSocket = new BybitWebSocket(tradeSeries);

        Thread websocketThread = new Thread(new BybitWebSocketClient(bybitWebSocket, "trade.BTCUSD"));
        websocketThread.start();

        // TODO consumer for tradeSeries

        System.out.println("the other thread");

    }
}