package com.metsuengine;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeEvent;

public class DifferenceIndicator implements Indicator {
    
    private final String name;
    private final TickSeries first;
    private final TickSeries second;
    private final Map<ZonedDateTime, Double> values;
    
    public DifferenceIndicator(String name, TickSeries first, TickSeries second) {
        this.name = name;
        this.first = first;
        this.second = second;
        this.first.addChangeListener(this);
        this.second.addChangeListener(this);
        this.values = new HashMap<ZonedDateTime, Double>();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        if (!first.isEmpty() && !second.isEmpty()) {
            values.put(source.getLastTick().time(), calculate());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double calculate() {
        fireStateChanged();
        return first.getLastTick().price() - second.getLastTick().price();
    }

    public double getValue() {
        if (!values.isEmpty()) {
            ZonedDateTime mostRecent = values.keySet().stream().max(new Comparator<ZonedDateTime>(){
    
                @Override
                public int compare(ZonedDateTime o1, ZonedDateTime o2) {
                    return o1.compareTo(o2);
                }
                
            }).get();
            return values.get(mostRecent);
        } else {
            return 0;
        }
    }
}
