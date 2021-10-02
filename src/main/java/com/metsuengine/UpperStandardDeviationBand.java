package com.metsuengine;

public class UpperStandardDeviationBand extends MovingAverage {
    
    private double value;
    private double multiple;

    public UpperStandardDeviationBand(int window, double multiple) {
        super(window);
        this.multiple = multiple;
    }
    
    @Override
    public void addTick(Tick tick) {
        descriptiveStatistics.addValue(tick.price());
        value = descriptiveStatistics.getMean()
            + (descriptiveStatistics.getStandardDeviation() * multiple);
        timeseries.put(tick.time(), value);
    }

    @Override
    public void addTickToTimeSeries(Tick tick) {
        descriptiveStatistics.addValue(tick.price());
        double value = descriptiveStatistics.getMean()
            + (descriptiveStatistics.getStandardDeviation() * multiple);
        timeseries.put(tick.time(), value);
    }
}
