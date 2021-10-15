package com.metsuengine;

import com.metsuengine.indicators.AverageIndicator;

public class Metsu {
    public static void main( String[] args ) {

        final TickSeries usd = new TickSeries("BTCUSD");
        final TickSeries tether = new TickSeries("BTCUSDT");
        final DifferenceIndicator difference = new DifferenceIndicator("Difference", usd, tether);
        final AverageIndicator averageDifference = new AverageIndicator("Average", difference);

        final TimeSeriesChart chart = new TimeSeriesChart("BTCUSD / BTCUSDT");
        chart.addTickSeries(usd);
        chart.addTickSeries(tether);
        chart.displayChart();

        final TimeSeriesChart differenceChart = new TimeSeriesChart("Difference");
        differenceChart.addIndicator(difference);
        differenceChart.addIndicator(averageDifference);
        differenceChart.displayChart();

        BybitWebSocketClient client = new BybitWebSocketClient(
            new SubscriptionSet(new BybitInversePerpetualTradeWebSocket(usd), 
                "wss://stream.bytick.com/realtime", 
                "trade.BTCUSD"),

            new SubscriptionSet(new BybitUSDTPerpetualTradeWebSocket(tether),
                "wss://stream.bytick.com/realtime_public",
                "trade.BTCUSDT"));
        
        client.run();

        // TODO - remove indicator class - just make single class OptimalBandsCalc using cointegration coeff
    }
}