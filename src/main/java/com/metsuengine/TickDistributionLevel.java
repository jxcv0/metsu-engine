package com.metsuengine;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TickDistributionLevel {
    
    private final List<Trade> ticks;

    public TickDistributionLevel(Trade initialTick) {
        this.ticks = new ArrayList<Trade>();
        addTick(initialTick);
    }

    public void addTick(Trade tick) {
        ticks.add(tick);
    }

    public double getVolume() {
        double total = 0;
        for (Trade tick : ticks) {
            total += tick.size();
        }
        return total;
    }

    public double getTotalDelta() {
        double total = 0;
        for (Trade tick : ticks) {
            total += tick.signedVolume();
        }
        return total;
    }

    public int getCount() {
        return ticks.size();
    }

    public List<Trade> getTicks() {
        return ticks;
    }

    public void removeByTime(int seconds) {
        List<Trade> toRemove = new ArrayList<Trade>();
        for (Trade tick : ticks) {
            if (tick.time().isBefore(ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(seconds))) {
                toRemove.add(tick);
            }
        }
        ticks.removeAll(toRemove);
    }
}
