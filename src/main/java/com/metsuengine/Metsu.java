package com.metsuengine;

import com.metsuengine.indicators.AverageIndicator;
import com.metsuengine.indicators.DifferenceIndicator;
import com.metsuengine.indicators.StandardDeviationIndicator;

public class Metsu {
    public static void main( String[] args ) {

        final TickSeries usd = new TickSeries("BTCUSD");
        final TickSeries tether = new TickSeries("BTCUSDT");
        final DifferenceIndicator differenceIndicator = new DifferenceIndicator("Difference", usd, tether);
        final AverageIndicator averageDifference = new AverageIndicator("Avergae Difference", differenceIndicator, 1000, usd, tether);
        final StandardDeviationIndicator stdDev = new StandardDeviationIndicator("SD", differenceIndicator, 1000, 2, usd, tether);
        final TimeSeriesChart chart = new TimeSeriesChart("BTCUSD / BTCUSDT", true, false);
        chart.addTickSeries(usd);
        chart.addTickSeries(tether);
        chart.addIndicator(averageDifference);
        chart.addIndicator(stdDev);
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