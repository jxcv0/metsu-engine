package com.metsuengine;

import com.metsuengine.WebSockets.CoinbaseSpotOrderbookWebSocket;
import com.metsuengine.WebSockets.CoinbaseSubscriptionSet;
import com.metsuengine.WebSockets.CoinbaseWebsocketClient;

public class Metsu {
    public static void main( String[] args ) {

        BybitRestAPIClient api = new BybitRestAPIClient("BTCUSD");
        try {
            for (Order order : api.getOrders()) {
                System.out.println(order.price() + order.orderStatus().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();    
        }
        

        // final MarketOrderBook orderBook = new MarketOrderBook();

        // new Controller(orderBook, 100000, 0.1, 400);

        // String[] channels = {"level2", "heartbeat"};
        // CoinbaseWebsocketClient client = new CoinbaseWebsocketClient(
        //     new CoinbaseSubscriptionSet(
        //         new CoinbaseSpotOrderbookWebSocket(orderBook), "BTC-USD", channels)
        // );

        // client.start();

    }
}