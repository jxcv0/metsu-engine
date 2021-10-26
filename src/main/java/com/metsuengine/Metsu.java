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
                if (orderBook.isReady()) {
                    int one = (int) orderBook.delta(0.001);
                    int two = (int) orderBook.delta(0.01);
                    int three = (int) orderBook.delta(0.1);
                    System.out.println(one + " " + two + " " + three);
                }
            }
            
        });

        String[] channels = {"level2", "heartbeat"};
        CoinbaseWebsocketClient client = new CoinbaseWebsocketClient(
            new CoinbaseSubscriptionSet(
                new CoinbaseSpotOrderbookWebSocket(orderBook), "BTC-USD", channels)
        );

        client.run();
    }
}