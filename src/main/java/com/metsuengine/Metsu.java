package com.metsuengine;

import java.time.ZonedDateTime;

public class Metsu {
    public static void main( String[] args ) {

        final MarketOrderBook orderBook = new MarketOrderBook();
        final TickSeries tickSeries = new TickSeries();

        Controller controller = new Controller(ZonedDateTime.now().toLocalDate().toString(),
            tickSeries,
            orderBook);

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