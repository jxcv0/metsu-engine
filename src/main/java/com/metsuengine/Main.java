package com.metsuengine;

import com.metsuengine.WebSockets.BybitInversePerpetualOrderBookWebsocket;
import com.metsuengine.WebSockets.BybitInversePerpetualSubscriptionSet;
import com.metsuengine.WebSockets.BybitInversePerpetualTradeWebSocket;
import com.metsuengine.WebSockets.BybitWebSocketClient;

public class Main {
    public static void main( String[] args ) {

        final TickSeries tickSeries = new TickSeries(10);
        final MarketOrderBook orderBook = new MarketOrderBook();

        new OrderMatchingStrategy(tickSeries, orderBook);

        BybitWebSocketClient client = new BybitWebSocketClient(
            new BybitInversePerpetualSubscriptionSet(
                new BybitInversePerpetualTradeWebSocket(tickSeries),
                    "wss://stream.bytick.com/realtime",
                    "trade.BTCUSD"),
            new BybitInversePerpetualSubscriptionSet(
                new BybitInversePerpetualOrderBookWebsocket(orderBook),
                    "wss://stream.bytick.com/realtime",
                    "orderBookL2_25.BTCUSD")
            
        );

        client.start();

    }
}