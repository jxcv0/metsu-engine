package com.metsuengine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TickDistribution implements ChangeListener {

    private final String name;
    private final Map<Double, TickDistributionLevel> distribution;
    private final int seconds;

    public TickDistribution(String name, TickSeries tickSeries, int seconds) {
        this.name = name;
        this.distribution = new ConcurrentHashMap<Double, TickDistributionLevel>();
        this.seconds = seconds;
        tickSeries.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        addTick(source.getLastTick());
    }

    public void addTick(Tick tick) {
        if (distribution.containsKey(tick.price())) {
            distribution.get(tick.price()).addTick(tick);
        } else {
            distribution.put(tick.price(), new TickDistributionLevel(tick));
        }
        trimExcessValues();
    }

    public double getVolumeAtPrice(double price) {
        return distribution.get(price).getVolume();
    }

    public double getDeltaAtPrice(double price) {
        return distribution.get(price).getTotalDelta();
    }

    public String getName() {
        return name;
    }

    public Map<Double, TickDistributionLevel> getLevels() {
        return this.distribution;
    }

    public int getTotalCount() {
        int count = 0;
        for (Double level : distribution.keySet()) {
            count += distribution.get(level).getCount();
        }
        return count;
    }

    private void trimExcessValues() {
        for (Double level : distribution.keySet()) {
            distribution.get(level).removeByTime(seconds);
        }
    }
}
