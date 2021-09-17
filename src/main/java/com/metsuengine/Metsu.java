package com.metsuengine;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Metsu {
    public static void main( String[] args ) {

        final VolumeProfile volumeProfile = new VolumeProfile();

        ZonedDateTime tomorrow = ZonedDateTime.now(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).plusDays(1);

        BybitWebSocket bybitWebSocket = new BybitWebSocket(volumeProfile);
        Thread websocketThread = new Thread(new BybitWebSocketClient(bybitWebSocket, "trade.BTCUSD"));
        websocketThread.start();

        volumeProfile.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println(
                    volumeProfile.getLastTrade().getTime() + " " +
                    volumeProfile.getLastTrade().getSide() + " " +
                    volumeProfile.getLastTrade().getPrice() + " " +
                    volumeProfile.getLastTrade().getSize() + " " +
                    volumeProfile.getSize()); // manage trades here?
            }            
        });

        try {
            Thread.sleep(20000);

            Chart chart = new Chart("Chart", "Volume Profile", volumeProfile);
            chart.displayChart();

            volumeProfile.writeVolumeProfile(tomorrow.minusDays(1));
            volumeProfile.writeAndPurge(tomorrow.minusDays(1));

        } catch (Exception e) {
            e.printStackTrace();
        }

        // if(ZonedDateTime.now(ZoneOffset.UTC).isEqual(tomorrow)) {
        //     tradeSeries.writeAndPurge(tomorrow);
        //     tomorrow = ZonedDateTime.now(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).plusDays(1);
        // }
    }
}