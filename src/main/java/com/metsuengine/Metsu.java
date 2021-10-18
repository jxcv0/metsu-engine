package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {

        final TickSeries btcusd = new TickSeries("BTCUSD", 10000);
        TickDistribution distribution = new TickDistribution("BTCUSD", btcusd);

        Chart chart = new Chart("Chart");
        chart.addTickSeries(btcusd);
        chart.addDistribution(distribution);
        chart.buildChart();

        BybitWebSocketClient client = new BybitWebSocketClient(
            new SubscriptionSet(new BybitInversePerpetualTradeWebSocket(btcusd), 
                "wss://stream.bytick.com/realtime", 
                "trade.BTCUSD"));
        
        client.run();
    }
}