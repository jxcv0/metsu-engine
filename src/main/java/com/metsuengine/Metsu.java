package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {

        final TickSeries btcusd = new TickSeries("BTCUSD", 10000);

        TickDistribution distribution = new TickDistribution("BTCUSD", btcusd);

        TimeSeriesChart chart = new TimeSeriesChart("Chart");
        chart.addTickSeries(btcusd);
        chart.addVolumeDistribution(distribution);
        chart.displayChart();

        BybitWebSocketClient client = new BybitWebSocketClient(
            new SubscriptionSet(new BybitInversePerpetualTradeWebSocket(btcusd), 
                "wss://stream.bytick.com/realtime", 
                "trade.BTCUSD"));
        
        client.run();
    }
}