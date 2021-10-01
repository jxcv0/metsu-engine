package com.metsuengine;

public class Metsu {
    public static void main( String[] args ) {

        final TradeSeries tradeSeries = new TradeSeries();

        //ZonedDateTime tomorrow = ZonedDateTime.now(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).plusDays(1);

        BybitWebSocket bybitWebSocket = new BybitWebSocket(tradeSeries);
        Thread websocketThread = new Thread(new BybitWebSocketClient(bybitWebSocket, "trade.BTCUSD"));
        websocketThread.start();

        // Something missing here

        try {
            Thread.sleep(120000);
            VolumeDistribution volumeDistribution = new VolumeDistribution(tradeSeries);
            DistributionChart chart = new DistributionChart("Chart", "Volume Distribution", volumeDistribution);
            chart.displayChart();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if(ZonedDateTime.now(ZoneOffset.UTC).isEqual(tomorrow)) {
        //     tradeSeries.writeAndPurge(tomorrow);
        //     tomorrow = ZonedDateTime.now(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).plusDays(1);
        // }
    }
}