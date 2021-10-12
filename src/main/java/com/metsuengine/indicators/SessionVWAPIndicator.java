package com.metsuengine.indicators;

import javax.swing.event.ChangeEvent;

import com.metsuengine.Indicator;
import com.metsuengine.TickSeries;

public class SessionVWAPIndicator implements Indicator {
    
    private double cumulativePrice;
    private double cumulativeVolume;

    public SessionVWAPIndicator() {
        this.cumulativeVolume = 0;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        cumulativePrice += (source.getLastTick().price() * source.getLastTick().size());
        cumulativeVolume += source.getLastTick().size();
    }

    @Override
    public double value(int index) {
        return cumulativePrice / cumulativeVolume;
    }
}
