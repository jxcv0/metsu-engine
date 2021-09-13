package com.metsuengine;

import java.io.Serializable;
import java.util.LinkedList;

public class TradeSeries implements Serializable {

    private LinkedList<Trade> tradeSeries;
    private int maxSize = Integer.MAX_VALUE;

    public TradeSeries() {
        tradeSeries = new LinkedList<Trade>();
    }

    public TradeSeries(LinkedList<Trade> tradeSeries) {
        this.tradeSeries = tradeSeries;
    }

    public TradeSeries(LinkedList<Trade> tradeSeries, int maxSize) {
        this.tradeSeries = tradeSeries;
        this.maxSize = maxSize;
    }

    public LinkedList<Trade> getTrades() {
        return this.tradeSeries;
    }

    public LinkedList<Trade> getTrades(int from, int to) {

        LinkedList<Trade> range = new LinkedList<Trade>();
        if (from > to) {
            for (int i = from; i < to; i++) {
                range.add(this.tradeSeries.get(i));
            }
        } else {
            throw new IllegalArgumentException("Start index must be greater than End index");
        }
        return range;
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

    public void addTrade(Trade trade) {
        this.tradeSeries.add(trade);
        manageSize();
    }
    
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    private void manageSize() {
        while (this.tradeSeries.size() > this.maxSize) {
            this.tradeSeries.removeFirst();
        }
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

    public double calculateDelta() {
        double delta = 0;
        for (Trade trade : this.tradeSeries) {
            if (trade.getSide().equals("Sell")) {
                delta -= trade.getSize();
            } else {
                delta += trade.getSize();
            }
        }
        return delta;
    }

    public double getSize() {
        return this.tradeSeries.size();
    }
}
