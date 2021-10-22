package com.metsuengine;

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

        BybitWebSocketClient client = new BybitWebSocketClient(
            new SubscriptionSet(new BybitInversePerpetualOrderBookWebsocket(orderBook),
            "wss://stream.bytick.com/realtime",
            "orderBook_200.100ms.BTCUSD")
        );

        client.run();

        while(true) {
            System.out.println(orderBook.size());
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}