package com.metsuengine.indicators;

import java.util.ArrayList;
import java.util.List;

import com.metsuengine.Indicator;
import com.metsuengine.TickSeries;

public abstract class AbstractIndicator<T> implements Indicator<T> {

    private final TickSeries tickSeries;
    private final List<T> values;
    
    /**
     * Constructor
     * 
     * @param tickSeries the TickSeries to calculate from
     */
    protected AbstractIndicator(TickSeries tickSeries) {
        this.tickSeries = tickSeries;
        this.values = new ArrayList<T>();
    }
    
    /**
     * @param index the Tick at which to calculate
     * @return the value of the indicator
     */
    protected abstract T calculate(int index);

    @Override
    public T value(int index) {
        return values.get(index);
    }

    @Override
    public TickSeries getTickSeries() {
        return tickSeries;
    }


}
