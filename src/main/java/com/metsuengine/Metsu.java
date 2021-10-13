package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {

        final TickSeries USDTickSeries = new TickSeries("BTCUSD");
        final TickSeries tetherTickSeries = new TickSeries("BTCUSDT");

        final TimeSeriesChart USDChart = new TimeSeriesChart(USDTickSeries.getname());
        final TimeSeriesChart tetherChart = new TimeSeriesChart(tetherTickSeries.getname());

        USDTickSeries.addChangeListener(USDChart);
        tetherTickSeries.addChangeListener(tetherChart);
        USDChart.displayChart();
        tetherChart.displayChart();

        BybitWebSocketClient client = new BybitWebSocketClient(
            new SubscriptionSet(new BybitInversePerpetualTradeWebSocket(USDTickSeries), 
                "wss://stream.bytick.com/realtime", 
                "trade.BTCUSD"),

            new SubscriptionSet(new BybitUSDTPerpetualTradeWebSocket(tetherTickSeries),
                "wss://stream.bytick.com/realtime_public",
                "trade.BTCUSDT")
        );
        
        client.run();
    }
}