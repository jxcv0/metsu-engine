package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {

        // 1.000346992

        final TickSeries usd = new TickSeries("BTCUSD", 10000);
        final TickSeries tether = new TickSeries("BTCUSDT", 10000);
        OptimalBandSelection bands = new OptimalBandSelection("Beta", usd, tether);

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