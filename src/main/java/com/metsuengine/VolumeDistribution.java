package com.metsuengine;

import java.util.HashMap;

public class VolumeDistribution {

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

    public HashMap<Double, Double> gaussian() {
        Gaussian gaussian = new Gaussian(standardDeviation(), vwap());
        HashMap<Double, Double> gaussianMap = new HashMap<Double, Double>();

        for (double level : this.map.keySet()) {
            gaussianMap.put(level, gaussian.value(this.map.get(level)));
        }

        return gaussianMap;
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
        double mean = vwap();

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
}
