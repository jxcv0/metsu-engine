package com.metsuengine;

import java.time.ZonedDateTime;
import java.util.HashMap;

public class StandardDeviationBandsPair {

    private UpperStandardDeviationBand upperBand;
    private LowerStandardDeviationBand lowerband;

    public StandardDeviationBandsPair(int window, double multiple) {
        this.upperBand = new UpperStandardDeviationBand(window, multiple);
        this.lowerband = new LowerStandardDeviationBand(window, multiple);
    }

    public void addTick(Tick tick) {
        upperBand.addTick(tick);
        lowerband.addTick(tick);
    }

    public void addTickToTimeSeries(Tick tick) {
        upperBand.addTickToTimeSeries(tick);
        lowerband.addTickToTimeSeries(tick);
    }

    public HashMap<ZonedDateTime, Double> getUpperBandTimeSeries() {
        return upperBand.getTimeSeries();
    }

    public HashMap<ZonedDateTime, Double> getLowerBandTimeSeries() {
        return lowerband.getTimeSeries();
    }
} 
