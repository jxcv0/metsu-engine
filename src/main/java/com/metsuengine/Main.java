package com.metsuengine;

import com.metsuengine.WebSockets.BybitInversePerpetualOrderBookWebsocket;
import com.metsuengine.WebSockets.BybitInversePerpetualSubscriptionSet;
import com.metsuengine.WebSockets.BybitInversePerpetualTradeWebSocket;
import com.metsuengine.WebSockets.BybitOrderWebSocket;
import com.metsuengine.WebSockets.BybitWebSocketClient;

public class Main {

    public static void main( String[] args ) {

        final TickSeries tickSeries = new TickSeries(10);
        final LimitOrderBook orderBook = new LimitOrderBook();
        final QuotePair quotes = new QuotePair();

        new OrderMatchingStrategy(tickSeries, orderBook, quotes);

        BybitWebSocketClient client = new BybitWebSocketClient(
            new BybitInversePerpetualSubscriptionSet(
                new BybitInversePerpetualTradeWebSocket(tickSeries),
                    "wss://stream.bytick.com/realtime",
                    "trade.BTCUSD"),
            new BybitInversePerpetualSubscriptionSet(
                new BybitInversePerpetualOrderBookWebsocket(orderBook),
                    "wss://stream-testnet.bytick.com/realtime",
                    "orderBookL2_25.BTCUSD"),
            new BybitInversePerpetualSubscriptionSet(
                new BybitOrderWebSocket(quotes),
                    "wss://stream-testnet.bytick.com/realtime",
                    "order")
        );

        client.start();
    }
}