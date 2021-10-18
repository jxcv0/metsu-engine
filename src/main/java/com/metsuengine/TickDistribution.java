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
        if (profile.containsKey(tick.price())) {
            profile.get(tick.price()).addTick(tick);
        } else {
            profile.put(tick.price(), new TickDistributionLevel(tick));
        }
    }

    public double getVolumeAtPrice(double price) {
        return profile.get(price).getVolume();
    }

    public double getDeltaAtPrice(double price) {
        return profile.get(price).getTotalDelta();
    }

    public String getName() {
        return name;
    }
}
