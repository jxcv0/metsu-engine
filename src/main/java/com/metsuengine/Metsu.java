package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {

        final MarketOrderBook orderBook = new MarketOrderBook("depth");
        final TickSeries tickSeries = new TickSeries("ticks");

        BybitWebSocketClient client = new BybitWebSocketClient(
            new SubscriptionSet(new BybitInversePerpetualOrderBookWebsocket(orderBook),
                "wss://stream.bytick.com/realtime",
                "orderBook_200.100ms.BTCUSD"),
            new SubscriptionSet(new BybitInversePerpetualTradeWebSocket(tickSeries),
                "wss://stream.bytick.com/realtime",
                "trade.BTCUSD")
        );

        client.run();
    }
}