package com.metsuengine;

public class Main {

    public static void main( String[] args ) {

        final String symbol = "BTCUSD";
        final TradeSeries tradeSeries = new TradeSeries(100);
        final LimitOrderBook orderBook = new LimitOrderBook();
        final Position position = new Position();

        final BybitAPIClient api = new BybitAPIClient(symbol);
        final OrderManager orders = new OrderManager(symbol, api);

        new Implimentation(tradeSeries, orderBook, orders, position);

        BybitWebSocketClient client = new BybitWebSocketClient(symbol, tradeSeries, orderBook, orders, position);
        client.start();
    }
}