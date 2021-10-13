package com.metsuengine.indicators;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.metsuengine.Indicator;
import com.metsuengine.TickSeries;

import org.jfree.data.time.Second;

public class AbstractIndicator implements Indicator {
    
    protected final String name;
    protected final Second maxLength;
    protected final Map<ZonedDateTime, Double> values;

    public AbstractIndicator(String name) {
        this.name = name;
        this.maxLength = null;
        this.values = new HashMap<ZonedDateTime, Double>();
    }

    public AbstractIndicator(String name, int maxLength) {
        this.name = name;
        this.maxLength = Second.parseSecond(Integer.toString(maxLength));
        this.values = new HashMap<ZonedDateTime, Double>();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        logger.error("Method unimplimented");
    }

    @Override
    public double value(ZonedDateTime index) {
        return values.get(index);
    }
    
    @Override
    public void calculate(TickSeries tickSeries) {
        logger.error("Method unimplimented");
    }

    public String getName() {
        return name;
    }

    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    public void fireStateChanged() {
        ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);
        if (listeners != null && listeners.length > 0) {
            ChangeEvent event = new ChangeEvent(this);
            for (ChangeListener listener : listeners) {
                listener.stateChanged(event);
            }
        }
    }
}
