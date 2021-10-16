package com.metsuengine.indicators;

import javax.swing.event.ChangeEvent;

import com.metsuengine.TickSeries;

public class DifferenceIndicator extends AbstractIndicator {
    
    private final TickSeries first;
    private final TickSeries second;
    
    public DifferenceIndicator(String name, TickSeries first, TickSeries second) {
        super(name);
        this.first = first;
        this.second = second;
        this.first.addChangeListener(this);
        this.second.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        if (!first.isEmpty() && !second.isEmpty()) {
            values.put(source.getLastTick().time(), calculate());
            fireStateChanged();
        }
    }

    @Override
    public double calculate() {
        return first.getLastTick().price() - second.getLastTick().price();
    }
}
