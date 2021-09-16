package com.metsuengine;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Metsu {
    public static void main( String[] args ) {

        final TradeSeries tradeSeries = new TradeSeries();
        ZonedDateTime tomorrow = ZonedDateTime.now(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).plusDays(1);
        BybitWebSocket bybitWebSocket = new BybitWebSocket(tradeSeries);

        Thread websocketThread = new Thread(new BybitWebSocketClient(bybitWebSocket, "trade.BTCUSD"));
        websocketThread.start();

        tradeSeries.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println(
                    tradeSeries.getLastTrade().getTime() + " " +
                    tradeSeries.getLastTrade().getSide() + " " +
                    tradeSeries.getLastTrade().getPrice() + " " +
                    tradeSeries.getLastTrade().getSize() + " " +
                    tradeSeries.getSize()); // manage trades here?
            }            
        });

        try {
            Thread.sleep(60000);
            tradeSeries.writeAndPurge(tomorrow.minusDays(1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO volume profile as separate class or as part of tradeseries??

        // if(ZonedDateTime.now(ZoneOffset.UTC).isEqual(tomorrow)) {
        //     tradeSeries.writeAndPurge(tomorrow);
        //     tomorrow = ZonedDateTime.now(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).plusDays(1);
        // }
    }
}