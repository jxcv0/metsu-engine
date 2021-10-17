package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BetaCoefficient implements ChangeListener {
    
    private final String name;
    private double value;
    private final TickSeries first;
    private final TickSeries second;
    
    public BetaCoefficient(String name, TickSeries first, TickSeries second) {
        this.name = name;
        this.first = first;
        this.second = second;
        this.first.addChangeListener(this);
        this.second.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (!first.isEmpty() && !second.isEmpty()) {
            value = first.getLastTick().price() / second.getLastTick().price();
        }
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
}
