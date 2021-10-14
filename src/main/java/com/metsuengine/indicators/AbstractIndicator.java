package com.metsuengine.indicators;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeEvent;

import com.metsuengine.Indicator;

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
        return calculate(index);
    }
    
    @Override
    public double calculate(ZonedDateTime index) {
        logger.error("Method unimplimented");
        return values.get(index);
    }

    public String getName() {
        return name;
    }
}
