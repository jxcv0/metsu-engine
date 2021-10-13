package com.metsuengine.indicators;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeEvent;

import com.metsuengine.Indicator;
import com.metsuengine.TickSeries;

import org.jfree.data.time.Second;

public class AbstractIndicator implements Indicator {
    
    private final Second maxLength;
    private final Map<ZonedDateTime, Double> values;

    public AbstractIndicator() {
        this.maxLength = null;
        this.values = new HashMap<ZonedDateTime, Double>();
    }

    public AbstractIndicator(int maxLength) {
        this.maxLength = Second.parseSecond(Integer.toString(maxLength));
        this.values = new HashMap<ZonedDateTime, Double>();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        values.put(source.getLastTick().time(), source.getLastTick().price());
    }

    @Override
    public double value(ZonedDateTime index) {
        return values.get(index);
    }

    // TODO - trim
}
