package com.metsuengine;

import java.time.ZonedDateTime;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class MovingAverage implements Indicator {
    
    protected double value;
    protected HashMap<ZonedDateTime, Double> timeseries;
    protected DescriptiveStatistics descriptiveStatistics;

    public MovingAverage(int window) {
        this.descriptiveStatistics = new DescriptiveStatistics(window);
        this.timeseries = new HashMap<ZonedDateTime, Double>();
    }

    public void addTick(Tick tick) {
        descriptiveStatistics.addValue(tick.price());
        value = descriptiveStatistics.getMean();
    }

    public void addTickToTimeSeries(Tick tick) {
        descriptiveStatistics.addValue(tick.price());
        value = descriptiveStatistics.getMean();
        timeseries.put(tick.time(), value);
    }

    public HashMap<ZonedDateTime, Double> getTimeSeries() {
        return timeseries;
    }

    public double value() {
        return value;
    }
}
