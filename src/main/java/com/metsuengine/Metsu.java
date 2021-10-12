package com.metsuengine;

import com.metsuengine.indicators.SessionVWAPIndicator;

public class Metsu {
    public static void main( String[] args ) {

        final TimeSeriesChart chart = new TimeSeriesChart("Live Test");
        final SessionVWAPIndicator vwap = new SessionVWAPIndicator();
        final TickSeries btcTickSeries = new TickSeries();
        btcTickSeries.addChangeListener(chart);
        btcTickSeries.addChangeListener(vwap);
        chart.displayChart();
        BybitWebSocketClient client = new BybitWebSocketClient(btcTickSeries, "trade.BTCUSD");
        client.run();
    }
}