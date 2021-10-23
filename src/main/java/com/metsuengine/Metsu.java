package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Metsu {
    public static void main( String[] args ) {

        // TickDistribution distribution = new TickDistribution("BTCUSD", btcusd, 1800);
        // DeltaSeries deltaSeries = new DeltaSeries("BTCUSD", 10);

        // Chart chart = new Chart("Chart");
        // chart.addTickSeries(btcusd);
        // chart.addDistribution(distribution);
        // chart.buildChart();

        // BybitWebSocketClient client = new BybitWebSocketClient(
        //     new SubscriptionSet(new BybitInversePerpetualTradeWebSocket(btcusd),
        //         "wss://stream.bytick.com/realtime",
        //         "trade.BTCUSD"));
        
        // client.run();

        final MarketOrderBook orderBook = new MarketOrderBook();
        orderBook.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                MarketOrderBook source = (MarketOrderBook) e.getSource();
                if (source.size() > 200) {
                    System.out.println(source.delta());                
                }
            }
        });

        BybitWebSocketClient client = new BybitWebSocketClient(
            new SubscriptionSet(new BybitInversePerpetualOrderBookWebsocket(orderBook),
            "wss://stream.bytick.com/realtime",
            "orderBook_200.100ms.BTCUSD")
        );

        client.run();
    }
}