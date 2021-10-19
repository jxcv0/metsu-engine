package com.metsuengine;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DeltaSeries implements ChangeListener {

    private final String name;
    private final Map<ZonedDateTime, Double> ticks;

    public DeltaSeries(String name, int seconds) {
        this.name = name;
        this.ticks = new HashMap<ZonedDateTime, Double>();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        addTick(source.getLastTick());
    }

    public void addTick(Tick tick) {
        ticks.put(tick.time(), tick.signedValue());
    }

    public double getDelta() {
        // return ticks.values().stream().mapToDouble(Double::doubleValue).sum();
        double delta = 0;
        for (Double x : ticks.values()) {
            delta += x;
        }
        return delta;
    }

    public String getName() {
        return name;
    }
}
