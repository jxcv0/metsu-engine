package com.metsuengine;

import java.time.ZonedDateTime;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class MovingAverage {
    
    protected HashMap<ZonedDateTime, Double> timeseries;
    protected DescriptiveStatistics descriptiveStatistics;

    public MovingAverage(int window) {
        this.descriptiveStatistics = new DescriptiveStatistics(window);
        this.timeseries = new HashMap<ZonedDateTime, Double>();
    }

    public void addTrade(Trade trade) {
        descriptiveStatistics.addValue(trade.price());
        timeseries.put(trade.time(), descriptiveStatistics.getMean());
    }

    public HashMap<ZonedDateTime, Double> getTimeSeries() {
        return timeseries;
    }
}
