package com.metsuengine;

import java.time.ZonedDateTime;
import java.util.HashMap;

public class VWAP {

    private double numerator;
    private double denominator;
    private HashMap<ZonedDateTime, Double> timeSeries;

    public VWAP() {
        this.numerator = 0;
        this.denominator = 0;
        this.timeSeries = new HashMap<ZonedDateTime, Double>();
    }

    public void increment(Trade trade) {
        this.numerator += trade.volumeByPrice();
        this.denominator += trade.size();
    }

    public void incrementAndStore(Trade trade) {
        increment(trade);
        timeSeries.put(trade.time(), this.value());
    }

    public double value() {
        return numerator/denominator;
    }

    public double rounded() {
        return round(this.value());
    }

    public void reset() {
        this.numerator = 0;
        this.denominator = 0;
    }

    private double round(double num) {
        return Math.round(num * 2) / 2.0;
    }

    public HashMap<ZonedDateTime, Double> getTimeSeries() {
        return timeSeries;
    }
}
