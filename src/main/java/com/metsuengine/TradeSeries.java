package com.metsuengine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

public class TradeSeries implements Serializable {

    private LinkedList<Trade> tradeSeries;

    public TradeSeries() {
        tradeSeries = new LinkedList<Trade>();
    }

    public TradeSeries(LinkedList<Trade> tradeSeries) {
        this.tradeSeries = tradeSeries;
    }

    public LinkedList<Trade> getTrades() {
        return this.tradeSeries;
    }

    public Trade getTrade(int index){
        return this.tradeSeries.get(index);
    } 

    public Trade getLastTrade() {
        if (!this.tradeSeries.isEmpty()) {
            return this.tradeSeries.getLast();
        } else {
            return null;
        }
    }

    public double getSize() {
        return this.tradeSeries.size();
    }

    public void addTrade(Trade trade) {
        this.tradeSeries.add(trade);
    }

    public void writeTradeToCSV(Trade trade) {
        String[] line = {
            trade.getTime().toString(),
            trade.getSide(),
            String.valueOf(trade.getPrice()),
            String.valueOf(trade.getSize())
        };

        CSVManager manager = new CSVManager("BTCUSD-trades.csv");
        manager.writeLine(line);
    }

    public HashMap<Double, Double> createMap(TradeSeries tradeSeries) {

        // TODO is there a better way?
        HashMap<Double, Double> map = new HashMap<Double, Double>();

        for (Trade trade : this.tradeSeries) {
            if (!map.containsKey(trade.getPrice())) {
                map.put(trade.getPrice(), trade.getSize());
            } else {
                double volume = map.get(trade.getPrice());
                map.put(trade.getPrice(), volume + trade.getSize());
            }
        }
        return map;
    }

    public double calculateVWAP() {
        double sumOfVolumeAtPice = 0;
        for (Trade trade : tradeSeries) {
            sumOfVolumeAtPice += (trade.getPrice() * trade.getSize());
        }
        return (sumOfVolumeAtPice / getSeriesVolume());
    }

    public double getSeriesVolume() {
        double total = 0;
        for(Trade trade : this.tradeSeries) {
            total += trade.getSize();
        }
        return total;
    }
}
