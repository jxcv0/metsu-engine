package com.metsuengine;

import com.metsuengine.indicators.MultivariateTimeSeriesIndicator;

public class Metsu {
    public static void main( String[] args ) {

        final TickSeries usd = new TickSeries("BTCUSD");
        final TickSeries tether = new TickSeries("BTCUSDT");
        final MultivariateTimeSeriesIndicator combined = new MultivariateTimeSeriesIndicator("Difference", usd, tether);

        final TimeSeriesChart chart = new TimeSeriesChart("BTCUSD / BTCUSDT", false);
        chart.addTickSeries(usd);
        chart.addTickSeries(tether);
        chart.addIndicator(combined);
        chart.displayChart();

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