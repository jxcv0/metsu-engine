package com.metsuengine;

import com.metsuengine.WebSockets.BybitInversePerpetualOrderBookWebsocket;
import com.metsuengine.WebSockets.BybitInversePerpetualSubscriptionSet;
import com.metsuengine.WebSockets.BybitInversePerpetualTradeWebSocket;
import com.metsuengine.WebSockets.BybitWebSocketClient;

public class Metsu {
    public static void main( String[] args ) {

        final MarketOrderBook orderBook = new MarketOrderBook();
        final TickSeries tickSeries = new TickSeries(10);

        Controller controller = new Controller(tickSeries, orderBook);

        BybitWebSocketClient client = new BybitWebSocketClient(
            new BybitInversePerpetualSubscriptionSet(new BybitInversePerpetualOrderBookWebsocket(orderBook),
                "wss://stream.bytick.com/realtime",
                "orderBook_200.100ms.BTCUSD"),
            new BybitInversePerpetualSubscriptionSet(new BybitInversePerpetualTradeWebSocket(tickSeries),
                "wss://stream.bytick.com/realtime",
                "trade.BTCUSD")
        );

        client.run();
    }
}