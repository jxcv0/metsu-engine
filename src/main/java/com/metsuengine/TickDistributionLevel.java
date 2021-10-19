package com.metsuengine;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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

    public int getCount() {
        return ticks.size();
    }

    public List<Tick> getTicks() {
        return ticks;
    }

    public void removeByTime(int seconds) {
        List<Tick> toRemove = new ArrayList<Tick>();
        for (Tick tick : ticks) {
            if (tick.time().isBefore(ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(seconds))) {
                toRemove.add(tick);
            }
        }
        ticks.removeAll(toRemove);
    }
}
