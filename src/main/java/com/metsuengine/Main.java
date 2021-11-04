package com.metsuengine;

public class Main {

    public static void main( String[] args ) {

        final TradeSeries tradeSeries = new TradeSeries(100);
        final LimitOrderBook orderBook = new LimitOrderBook();
        final QuotePair quotes = new QuotePair();
        final Position position = new Position();
       
        BybitWebSocketClient client = new BybitWebSocketClient("BTCUSD", tradeSeries, orderBook, quotes, position);

        new OrderManager(tradeSeries, orderBook, quotes, position, new Model(tradeSeries, 0.1, 0.1));

        client.start();
    }
}