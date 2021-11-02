package com.metsuengine;

import com.metsuengine.WebSockets.BybitInversePerpetualOrderBookWebsocket;
import com.metsuengine.WebSockets.BybitInversePerpetualSubscriptionSet;
import com.metsuengine.WebSockets.BybitInversePerpetualTradeWebSocket;
import com.metsuengine.WebSockets.BybitOrderWebSocket;
import com.metsuengine.WebSockets.BybitPositionWebSocket;
import com.metsuengine.WebSockets.BybitWebSocketClient;

public class Main {

    public static void main( String[] args ) {

        final TickSeries tickSeries = new TickSeries(10);
        final LimitOrderBook orderBook = new LimitOrderBook();
        final QuotePair quotes = new QuotePair();
        final Position position = new Position();

        BybitWebSocketClient client = new BybitWebSocketClient(
            new BybitInversePerpetualSubscriptionSet(
                new BybitOrderWebSocket(quotes),
                    "wss://stream.bytick.com/realtime",
                    "order"),
            new BybitInversePerpetualSubscriptionSet(
                new BybitPositionWebSocket(position),
                    "wss://stream.bytick.com/realtime",
                    "position"),
            new BybitInversePerpetualSubscriptionSet(
                new BybitInversePerpetualTradeWebSocket(tickSeries),
                    "wss://stream.bytick.com/realtime",
                    "trade.BTCUSD"),
            new BybitInversePerpetualSubscriptionSet(
                new BybitInversePerpetualOrderBookWebsocket(orderBook),
                    "wss://stream.bytick.com/realtime",
                    "orderBookL2_25.BTCUSD"));

        new OrderManager(tickSeries, orderBook, quotes, new GlostenMilgrom(0.2));

        client.start();
    }
}