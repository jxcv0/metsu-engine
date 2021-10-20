package com.metsuengine;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
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
        add(source.getLastTick());
    }

    public void add(Tick tick) {
        ticks.put(tick.time(), tick.signedVolume());
    }

    public double getDelta() {
        return ticks.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public String getName() {
        return name;
    }

    public static double calculate(List<Tick> ticks) {
        double delta = 0;
        for (Tick tick : ticks) {
            delta += tick.signedVolume();
        }
        return delta;
    }
}
