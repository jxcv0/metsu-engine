package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {

        CSVManager manager = new CSVManager("BTCUSD2021-10-15.csv");

        final TickSeries btcusd = manager.createTickSeries();
        
        for (int i = 5; i < btcusd.getSize(); i++) {
            double test = DeltaSeries.calculate(btcusd.getSubSeries(i, i+5));
            System.out.println(test);
        }

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
    }
}