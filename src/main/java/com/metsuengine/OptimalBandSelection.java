package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class OptimalBandSelection implements ChangeListener {
    
    private final String name;
    private final DescriptiveStatistics descriptiveStatistics;
    private final TickSeries first;
    private final TickSeries second;
    private double sdUpper;
    private double sdLower;
    
    public OptimalBandSelection(String name, TickSeries first, TickSeries second) {
        this.name = name;
        this.descriptiveStatistics = new DescriptiveStatistics();
        this.first = first;
        this.second = second;
        this.first.addChangeListener(this);
        this.second.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (!first.isEmpty() && !second.isEmpty()) {
            descriptiveStatistics.addValue(first.getLastTick().price() / second.getLastTick().price());
            sdUpper = descriptiveStatistics.getMean() + (descriptiveStatistics.getStandardDeviation()*2);
            sdLower = descriptiveStatistics.getMean() - (descriptiveStatistics.getStandardDeviation()*2);

            System.out.println(sdUpper + " " + sdLower);
        }
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        //TODO
        return 0;
    }
}
