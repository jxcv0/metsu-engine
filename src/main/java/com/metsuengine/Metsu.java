package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {

        // TODO - make Websocket classes into one that handles all subscriptions
        // use individual Parser classes/methods to parse each part
        // need some way of assigning Tickseries to each Parser

        final TickSeries USDTickSeries = new TickSeries("BTCUSD");
        final TickSeries tetherTickSeries = new TickSeries("BTCUSDT");

        final TimeSeriesChart USDChart = new TimeSeriesChart(USDTickSeries.getname());
        final TimeSeriesChart tetherChart = new TimeSeriesChart(tetherTickSeries.getname());

        USDTickSeries.addChangeListener(USDChart);
        tetherTickSeries.addChangeListener(tetherChart);
        USDChart.displayChart();
        tetherChart.displayChart();

        BybitWebSocketClient USDclient = new BybitWebSocketClient(
            new BybitInversePerpetualTradeWebSocket(USDTickSeries),
            "wss://stream.bytick.com/realtime",
            "trade.BTCUSD");
        
        BybitWebSocketClient USDTclient = new BybitWebSocketClient(
            new BybitUSDTPerpetualTradeWebSocket(tetherTickSeries),
            "wss://stream.bytick.com/realtime_public",
            "trade.BTCUSDT");
        
        USDclient.run();
        USDTclient.run();
    }
}