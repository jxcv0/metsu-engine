package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {

        final TickSeries usd = new TickSeries("BTCUSD");
        final TickSeries tether = new TickSeries("BTCUSDT");
        final DifferenceIndicator difference = new DifferenceIndicator("Difference", usd, tether);

        final TimeSeriesChart chart = new TimeSeriesChart("BTCUSD / BTCUSDT");
        chart.addTickSeries(usd);
        chart.addTickSeries(tether);
        chart.displayChart();

        final TimeSeriesChart diferenceChart = new TimeSeriesChart("Difference");
        diferenceChart.addIndicator(difference);
        diferenceChart.displayChart();

        BybitWebSocketClient client = new BybitWebSocketClient(
            new SubscriptionSet(new BybitInversePerpetualTradeWebSocket(usd), 
                "wss://stream.bytick.com/realtime", 
                "trade.BTCUSD"),

            new SubscriptionSet(new BybitUSDTPerpetualTradeWebSocket(tether),
                "wss://stream.bytick.com/realtime_public",
                "trade.BTCUSDT"));
        
        client.run();
    }
}