package com.metsuengine;

public class UpperStandardDeviationBand extends MovingAverage {
    
    private double value;
    private double multiple;

    public UpperStandardDeviationBand(int window, double multiple) {
        super(window);
        this.multiple = multiple;
    }
    
    @Override
    public void addTrade(Trade trade) {
        descriptiveStatistics.addValue(trade.price());
        value = descriptiveStatistics.getMean()
            + (descriptiveStatistics.getStandardDeviation() * multiple);
        timeseries.put(trade.time(), value);
    }

    @Override
    public void addTradeToTimeSeries(Trade trade) {
        descriptiveStatistics.addValue(trade.price());
        double value = descriptiveStatistics.getMean()
            + (descriptiveStatistics.getStandardDeviation() * multiple);
        timeseries.put(trade.time(), value);
    }
}
