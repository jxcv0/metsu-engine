package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Metsu {
    public static void main( String[] args ) {

        final TradeSeries tradeSeries = new TradeSeries();

        //ZonedDateTime tomorrow = ZonedDateTime.now(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).plusDays(1);

        BybitWebSocket bybitWebSocket = new BybitWebSocket(tradeSeries);
        Thread websocketThread = new Thread(new BybitWebSocketClient(bybitWebSocket, "trade.BTCUSD"));
        websocketThread.start();

        tradeSeries.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println(
                    // TODO update VP + Strat here
                    tradeSeries.getLastTrade().getTime() + " " +
                    tradeSeries.getLastTrade().getSide() + " " +
                    tradeSeries.getLastTrade().getPrice() + " " +
                    tradeSeries.getLastTrade().getSize() + " " +
                    tradeSeries.getSize());
            }            
        });

        try {
            Thread.sleep(120000);
            VolumeDistribution volumeDistribution = new VolumeDistribution(tradeSeries);
            Chart chart = new Chart("Chart", "Volume Distribution", volumeDistribution);
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