package com.metsuengine;

import com.metsuengine.WebSockets.CoinbaseSpotOrderbookWebSocket;
import com.metsuengine.WebSockets.CoinbaseSubscriptionSet;
import com.metsuengine.WebSockets.CoinbaseWebsocketClient;

public class Metsu {
    public static void main( String[] args ) {

        final MarketOrderBook orderBook = new MarketOrderBook();

        new Controller(orderBook);

        String[] channels = {"level2", "heartbeat"};
        CoinbaseWebsocketClient client = new CoinbaseWebsocketClient(
            new CoinbaseSubscriptionSet(
                new CoinbaseSpotOrderbookWebSocket(orderBook), "BTC-USD", channels)
        );

        client.start();

    }
}