package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {

        final TickSeries usd = new TickSeries("BTCUSD");
        final TickSeries tether = new TickSeries("BTCUSDT");
        BetaCoefficient cfc = new BetaCoefficient("Beta", usd, tether);

        final TimeSeriesChart chart = new TimeSeriesChart("BTCUSD / BTCUSDT", false);
        chart.addTickSeries(tether);
        chart.addTickSeries(usd);
        chart.addToAlternativeDataset(cfc);
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