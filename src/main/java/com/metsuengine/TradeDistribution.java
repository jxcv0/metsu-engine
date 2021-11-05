package com.metsuengine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TradeDistribution implements ChangeListener {

    private final String name;
    private final Map<Double, TradeDistributionLevel> distribution;
    private final int seconds;

    public TradeDistribution(String name, TradeSeries tradeSeries, int seconds) {
        this.name = name;
        this.distribution = new ConcurrentHashMap<Double, TradeDistributionLevel>();
        this.seconds = seconds;
        tradeSeries.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TradeSeries source = (TradeSeries) e.getSource();
        addtrade(source.lasttrade());
    }

    public void addtrade(Trade trade) {
        if (distribution.containsKey(trade.price())) {
            distribution.get(trade.price()).addtrade(trade);
        } else {
            distribution.put(trade.price(), new TradeDistributionLevel(trade));
        }
        trimExcessValues();
    }

    public double getVolumeAtPrice(double price) {
        return distribution.get(price).getVolume();
    }

    public double getDeltaAtPrice(double price) {
        return distribution.get(price).getTotalDelta();
    }

    public String getName() {
        return name;
    }

    public Map<Double, TradeDistributionLevel> getLevels() {
        return this.distribution;
    }

    public int getTotalCount() {
        int count = 0;
        for (Double level : distribution.keySet()) {
            count += distribution.get(level).getCount();
        }
        return count;
    }

    private void trimExcessValues() {
        for (Double level : distribution.keySet()) {
            distribution.get(level).removeByTime(seconds);
        }
    }
}
