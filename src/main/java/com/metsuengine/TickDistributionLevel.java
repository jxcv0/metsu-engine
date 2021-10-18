package com.metsuengine;

import java.util.ArrayList;
import java.util.List;

public class TickDistributionLevel {
    
    private final List<Tick> ticks;

    public TickDistributionLevel(Tick initialTick) {
        this.ticks = new ArrayList<Tick>();
        addTick(initialTick);
    }

    public void addTick(Tick tick) {
        ticks.add(tick);
    }

    public double getVolume() {
        double total = 0;
        for (Tick tick : ticks) {
            total += tick.size();
        }
        return total;
    }

    public double getTotalDelta() {
        double total = 0;
        for (Tick tick : ticks) {
            total += tick.signedValue();
        }
        return total;
    }

    public double getCount() {
        return ticks.size();
    }
}
