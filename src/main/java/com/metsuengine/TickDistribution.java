package com.metsuengine;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TickDistribution implements ChangeListener {

    private final String name;
    private final Map<Double, TickDistributionLevel> profile;

    public TickDistribution(String name, TickSeries tickSeries) {
        this.name = name;
        this.profile = new TreeMap<Double, TickDistributionLevel>();
        tickSeries.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        addTick(source.getLastTick());
    }

    public void addTick(Tick tick) {
        profile.get(tick.price()).addTick(tick);
    }

    public double getVolumeAtPrice(double price) {
        return profile.get(price).getVolume();
    }

    public String getName() {
        return name;
    }
}
