package com.metsuengine;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TradeDistributionLevel {
    
    private final List<Trade> trades;

    public TradeDistributionLevel(Trade initialtrade) {
        this.trades = new ArrayList<Trade>();
        addtrade(initialtrade);
    }

    public void addtrade(Trade trade) {
        trades.add(trade);
    }

    public double getVolume() {
        double total = 0;
        for (Trade trade : trades) {
            total += trade.size();
        }
        return total;
    }

    public double getTotalDelta() {
        double total = 0;
        for (Trade trade : trades) {
            total += trade.signedVolume();
        }
        return total;
    }

    public int getCount() {
        return trades.size();
    }

    public List<Trade> gettrades() {
        return trades;
    }

    public void removeByTime(int seconds) {
        List<Trade> toRemove = new ArrayList<Trade>();
        for (Trade trade : trades) {
            if (trade.time().isBefore(ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(seconds))) {
                toRemove.add(trade);
            }
        }
        trades.removeAll(toRemove);
    }
}
