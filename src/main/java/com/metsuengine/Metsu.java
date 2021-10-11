package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {
        final TickSeries btcTickSeries = new TickSeries();
        TickListener tickListener = new TickListener();
        btcTickSeries.addChangeListener(tickListener);
        BybitWebSocketClient client = new BybitWebSocketClient(new BybitWebSocket(btcTickSeries), "trade.BTCUSD");
        client.run();
    }
}