package com.metsuengine;

public class Main {

    public static void main( String[] args ) {

        final TradeSeries tradeSeries = new TradeSeries(100);
        final LimitOrderBook orderBook = new LimitOrderBook();
        final OrderManager orders = new OrderManager();
        final Position position = new Position();
        final Model model = new Model(tradeSeries, 0.1, 0.3);

        new Strategy(tradeSeries, orderBook, orders, position, model);

        BybitWebSocketClient client = new BybitWebSocketClient("BTCUSD", tradeSeries, orderBook, orders, position);
        client.start();
    }
}