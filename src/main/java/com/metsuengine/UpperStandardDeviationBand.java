package com.metsuengine;

public class UpperStandardDeviationBand extends MovingAverage {

    private double multiple;

    public UpperStandardDeviationBand(int window, double multiple) {
        super(window);
        this.multiple = multiple;
    }
    
    @Override
    public void addTrade(Trade trade) {
        descriptiveStatistics.addValue(trade.price());
        double value = descriptiveStatistics.getMean()
            + (descriptiveStatistics.getStandardDeviation() * multiple);
        timeseries.put(trade.time(), value);
    }
}
