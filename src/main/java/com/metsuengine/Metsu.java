package com.metsuengine;

import java.text.DecimalFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;

public class Metsu {
    public static void main( String[] args ) {

        CSVManager csvManager = new CSVManager("BTCUSD-trades.csv");
        BybitEndpoint endpoint = new BybitEndpoint("BTCUSD");
        TradeSeries tradeSeries = endpoint.getTradingRecords();
        
        for (Trade trade : tradeSeries.getTrades()) {
            String[] line = {
                trade.getTime().toString(),
                trade.getSide(),
                Double.toString(trade.getPrice()),
                Double.toString(trade.getSize()),
            };
            csvManager.writeLine(line);
        }

        // BybitWebSocket bybitWebSocket = new BybitWebSocket(tradeSeries);
        // Thread websocketThread = new Thread(new BybitWebSocketClient(bybitWebSocket, "trade.BTCUSD"));
        // websocketThread.start();

    }
}