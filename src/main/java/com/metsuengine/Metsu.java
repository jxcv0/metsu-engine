package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {

        TimeSeriesChart chart = new TimeSeriesChart("Live Test");

        final TickSeries btcTickSeries = new TickSeries();
        btcTickSeries.addChangeListener(chart);
        chart.displayChart();
        BybitWebSocketClient client = new BybitWebSocketClient(new BybitWebSocket(btcTickSeries), "trade.BTCUSD");
        client.run();
    }
}