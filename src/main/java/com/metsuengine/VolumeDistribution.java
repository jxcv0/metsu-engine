package com.metsuengine;

import java.util.HashMap;

import org.apache.commons.math3.distribution.NormalDistribution;

public class VolumeDistribution extends NormalDistribution {

    private HashMap<Double, Double> map = new HashMap<>();

    public VolumeDistribution() {
        this.map = new HashMap<Double, Double>();
    }

    public VolumeDistribution(TradeSeries tradeSeries) {
        createVolumeProfile(tradeSeries);
    }

    public HashMap<Double, Double> getHashMap() {
        return this.map;
    }

    public void createVolumeProfile(TradeSeries tradeSeries) {
        for (Trade trade : tradeSeries.getTrades()) {
            update(trade);
        }
    }

    public void update(Trade trade) {
        double price = trade.getPrice();
        double size = trade.getSize();

        if(this.map.containsKey(price)) {
            double oldSize = this.map.get(price);
            this.map.put(price, oldSize + price);
        } else {
            this.map.put(price, size);
        }
    }

    public HashMap<Double, Double> densityMap() {
        HashMap<Double, Double> pdMap = new HashMap<Double, Double>();

        for (double level : this.map.keySet()) {
            pdMap.put(level, this.map.get(this.density(level)));
            System.out.println(this.map.get(this.density(level)));
        }
        return pdMap;
    }
}
