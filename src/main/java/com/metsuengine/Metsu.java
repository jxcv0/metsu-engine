package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.metsuengine.WebSockets.CoinbaseSpotOrderbookWebSocket;
import com.metsuengine.WebSockets.CoinbaseSubscriptionSet;
import com.metsuengine.WebSockets.CoinbaseWebsocketClient;

public class Metsu {
    public static void main( String[] args ) {

        final MarketOrderBook orderBook = new MarketOrderBook();

        orderBook.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                MarketOrderBook orderBook = (MarketOrderBook) e.getSource();
                for (double level : orderBook.map().keySet()) {
                    if (orderBook.map().get(level) < 0) {
                        System.out.println(level + " " +  orderBook.map().get(level));
                    }
                }
            }
            
        });

        CoinbaseWebsocketClient client = new CoinbaseWebsocketClient(
            new CoinbaseSubscriptionSet(new CoinbaseSpotOrderbookWebSocket(orderBook),
            "BTC-USDT", "level2")
        );

        client.run();
    }
}