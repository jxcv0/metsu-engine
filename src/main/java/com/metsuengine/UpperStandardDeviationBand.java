package com.metsuengine;

public class UpperStandardDeviationBand extends MovingAverage {

    public UpperStandardDeviationBand(int window) {
        super(window);
    }
    
    @Override
    public void addTrade(Trade trade) {
        descriptiveStatistics.addValue(trade.price());
        double value = descriptiveStatistics.getMean() + descriptiveStatistics.getStandardDeviation();
        timeseries.put(trade.time(), value);
    }
}
