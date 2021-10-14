package com.metsuengine.indicators;

import java.time.ZonedDateTime;

import javax.swing.event.ChangeEvent;

import com.metsuengine.TickSeries;

public class DifferenceIndicator extends AbstractIndicator {

    private TickSeries first;
    private TickSeries second;

    public DifferenceIndicator(String name, TickSeries first, TickSeries second) {
        super(name);
        this.first = first;
        this.second = second;
        first.addChangeListener(this);
        second.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (first.getSize() > 0 && second.getSize() > 0) {
            TickSeries source = (TickSeries) e.getSource();
            ZonedDateTime time = source.getLastTick().time();
            values.put(time, calculate(time));
        }
    }

    @Override
    public double calculate(ZonedDateTime index) {
        return first.getLastTick().price() - second.getLastTick().price();
    }
}
