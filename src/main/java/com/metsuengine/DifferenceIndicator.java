package com.metsuengine;

import javax.swing.event.ChangeEvent;

public class DifferenceIndicator implements Indicator {
    
    private final String name;
    private final TickSeries first;
    private final TickSeries second;
    
    public DifferenceIndicator(String name, TickSeries first, TickSeries second) {
        this.name = name;
        this.first = first;
        this.second = second;
        this.first.addChangeListener(this);
        this.second.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        if (!first.isEmpty() && !second.isEmpty()) {
            System.out.println(source.getLastTick().time() + " " + calculate());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double calculate() {
        return first.getLastTick().price() - second.getLastTick().price();
    }
}
