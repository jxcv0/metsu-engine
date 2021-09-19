package com.metsuengine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

public class VolumeDistribution {

    private HashMap<Double, Double> map = new HashMap<>();

    public VolumeDistribution() {
        this.map = new HashMap<Double, Double>();
    }

    public VolumeDistribution(TradeSeries tradeSeries) {
        createVolumeProfile(tradeSeries);
    }

    public HashMap<Double, Double> toHashMap() {
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

    public HashMap<Double, Double> filter() {
        HashMap<Double, Double> filteredMap = new HashMap<Double, Double>();

        return filteredMap;
    }

    public double vwap() {
        double sumOfVolumeAtPice = 0;
        for (Double level : this.map.keySet()) {
            sumOfVolumeAtPice += (level * map.get(level));
        }
        return (sumOfVolumeAtPice / getTotalVolume());
    }

    public double standardDeviation() {
        double sumOfxMinusMean = 0;
        double mean = this.pointOfControl();

        for (double level : this.map.keySet()) {
            double levelMinusMean = (this.map.get(level) - mean);
            sumOfxMinusMean += (levelMinusMean * levelMinusMean);
        }

        return Math.sqrt(sumOfxMinusMean/getTotalVolume());
    }

    public double getTotalVolume() {
        double total = 0;
        for(double level : this.map.keySet()) {
            total += this.map.get(level);
        }
        return total;
    }

    public double pointOfControl() { 
        double poc = Collections.max(this.map.values());

        for (Entry<Double, Double> entry : this.map.entrySet()) {
            if (entry.getValue() == poc) {
                return entry.getKey();
            }
        }
        
        return 0;
    }
}
