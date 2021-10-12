package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {

        final TimeSeriesChart chart = new TimeSeriesChart("BTC USD/USDT");
        //final TickSeries USDTickSeries = new TickSeries();
        final TickSeries tetherTickSeries = new TickSeries();

        //USDTickSeries.addChangeListener(chart);
        tetherTickSeries.addChangeListener(chart);
        chart.displayChart();

        // BybitWebSocketClient USDclient = new BybitWebSocketClient(
        //     new BybitInversePerpetualTradeWebSocket(USDTickSeries),
        //     "wss://stream.bytick.com/realtime",
        //     "trade.BTCUSD");
        
        BybitWebSocketClient USDTclient = new BybitWebSocketClient(
            new BybitUSDTPerpetualTradeWebSocket(tetherTickSeries),
            "wss://stream.bytick.com/realtime_public",
            "trade.BTCUSDT");
        
        //USDclient.run();
        USDTclient.run();
    }
}