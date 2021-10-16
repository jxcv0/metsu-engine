package com.metsuengine.indicators;

import javax.swing.event.ChangeEvent;

import com.metsuengine.TickSeries;

public class MultivariateTimeSeriesIndicator extends AbstractIndicator {

    public MultivariateTimeSeriesIndicator(String name, TickSeries...series) {
        super(name);
        for (TickSeries tickSeries : series) {
            tickSeries.addChangeListener(this);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries tickSeries = (TickSeries) e.getSource();
        values.put(tickSeries.getLastTick().time(), tickSeries.getLastTick().price());
    }
}
