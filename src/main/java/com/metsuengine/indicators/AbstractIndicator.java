package com.metsuengine.indicators;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.event.ChangeEvent;

import com.metsuengine.Indicator;

public abstract class AbstractIndicator implements Indicator {

    protected final String name;
    protected final Map<ZonedDateTime, Double> values;

    public AbstractIndicator(String name) {
        this.name = name;
        this.values = new ConcurrentHashMap<ZonedDateTime, Double>();
    }

    public abstract void stateChanged(ChangeEvent e);

    public String getName() {
        return name;
    }

    public double getValue() {
        return isEmpty() ? 0 : values.get(getTime());
    }

    public ZonedDateTime getTime() {
        ZonedDateTime mostRecent = values.keySet().stream().max(new Comparator<ZonedDateTime>(){

            @Override
            public int compare(ZonedDateTime o1, ZonedDateTime o2) {
                return o1.compareTo(o2);
            }
            
        }).get();
        return mostRecent;
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public double[] toArray() {
        List<Double> listValues = values.values().stream().toList();
        return listValues.stream().mapToDouble(Double::doubleValue).toArray();
    }
}
