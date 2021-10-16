package com.metsuengine;

import com.metsuengine.indicators.DifferenceIndicator;

public class Metsu {
    public static void main( String[] args ) {

        final TickSeries usd = new TickSeries("BTCUSD");
        final TickSeries tether = new TickSeries("BTCUSDT");
        final DifferenceIndicator differenceIndicator = new DifferenceIndicator("Difference", usd, tether);

        final TimeSeriesChart chart = new TimeSeriesChart("BTCUSD / BTCUSDT");
        chart.addTickSeries(usd);
        chart.addTickSeries(tether);
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