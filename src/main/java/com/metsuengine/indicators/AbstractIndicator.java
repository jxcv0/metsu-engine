package com.metsuengine.indicators;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.event.ChangeEvent;

import com.metsuengine.Indicator;

public class AbstractIndicator implements Indicator {

    protected final String name;
    protected final Map<ZonedDateTime, Double> values;

    public AbstractIndicator(String name) {
        this.name = name;
        this.values = new ConcurrentHashMap<ZonedDateTime, Double>();
    }

    public void stateChanged(ChangeEvent e) {
        System.out.println("Unimplimented Method");
    }

    public String getName() {
        return name;
    }

    public double calculate() {
        fireStateChanged();
        return 0;
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
        return values.size() < 1;
    }
}
