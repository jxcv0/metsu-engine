package com.metsuengine;

public class VWAP {

    private double numerator;
    private double denominator;

    public VWAP() {
        this.numerator = 0;
        this.denominator = 0;
    }

    public void increment(Trade trade) {
        this.numerator += trade.volumeByPrice();
        this.denominator += trade.size();
    }

    public double value() {
        return numerator/denominator;
    }

    public void reset() {
        this.numerator = 0;
        this.denominator = 0;
    }
}
