package com.metsuengine;

import java.io.Serializable;
import java.util.LinkedList;

public class TradeSeries implements Serializable {

    private LinkedList<Trade> tradeSeries = new LinkedList<Trade>();
    private int maxSize = Integer.MAX_VALUE;

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
}
