package com.metsuengine;

import com.metsuengine.Enums.OrderStatus;
import com.metsuengine.Enums.OrderType;
import com.metsuengine.Enums.Side;
import com.metsuengine.Enums.TimeInForce;
import com.metsuengine.WebSockets.CoinbaseSpotOrderbookWebSocket;
import com.metsuengine.WebSockets.CoinbaseSubscriptionSet;
import com.metsuengine.WebSockets.CoinbaseWebsocketClient;

public class Main {
    public static void main( String[] args ) {

        Order order = new Order("BTCUSD", Side.Buy, OrderType.Limit, 58000, 1, TimeInForce.GoodTillCancel, OrderStatus.New, "SUCCESS!");

        BybitRestAPIClient api = new BybitRestAPIClient("BTCUSD");
        try {
            api.placeOrder(order);
            for (Order o : api.getOrders()) {
                if (o.orderLinkId().length() > 0) {
                    System.out.println(o.orderLinkId());
                }
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