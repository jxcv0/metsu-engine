package com.metsuengine;

public class Main {

    public static void main( String[] args ) {

        final String symbol = "BTCUSD";
        final TradeSeries tradeSeries = new TradeSeries(1000);
        final LimitOrderBook orderBook = new LimitOrderBook();
        final Position position = new Position();

        final BybitAPIClient api = new BybitAPIClient(symbol);
        final OrderManager orders = new OrderManager(symbol, api);

        final AvellanedaStoikovModel model = new AvellanedaStoikovModel(tradeSeries, orderBook, 0.01, 0.25, 250);

        new Implimentation(tradeSeries, orderBook, orders, position, model);

        BybitWebSocketClient client = new BybitWebSocketClient(symbol, tradeSeries, orderBook, orders, position);
        client.start();
    }
}