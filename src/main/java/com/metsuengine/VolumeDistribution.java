package com.metsuengine;

import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.interpolation.LoessInterpolator;

public class VolumeDistribution extends TreeMap<Double, Double> {

    public VolumeDistribution() {
        super();
    }

    public VolumeDistribution(TradeSeries tradeSeries) {
        createVolumeProfile(tradeSeries);
    }

    public void createVolumeProfile(TradeSeries tradeSeries) {
        for (Trade trade : tradeSeries.getTrades()) {
            update(trade);
        }
    }

    public void update(Trade trade) {
        double price = trade.getPrice();
        double size = trade.getSize();

        if(this.containsKey(price)) {
            double oldSize = this.get(price);
            this.put(price, oldSize + price);
        } else {
            this.put(price, size);
        }
    }

    public void normalize() {
        double min = this.minValue();
        double max = this.maxValue();
        for (double level : this.keySet()) {
            this.put(level, norm(this.get(level), min, max));
        }
    }

    public void pdf() {
        Gaussian gaussian = new Gaussian(this.maxValue(), this.standardDeviation());
        for (double  level : this.keySet()) {
            this.put(level, gaussian.value(this.get(level)));
        }
    }

    private double norm(double x, double min, double max) {
        return (x - min) / (max - min);
    }

    public void filter() {
        LoessInterpolator interpolator = new LoessInterpolator(0.02, 2);
        double[] values = interpolator.smooth(this.keysToArray(), this.valuesToArray());
        double[] keys = this.keysToArray();

        for (int i = 0; i < values.length; i++) {
            this.put(keys[i], values[i]);
        }
    }

    public double vwap() {
        double sumOfVolumeAtPice = 0;
        for (double level : this.keySet()) {
            sumOfVolumeAtPice += (level * this.get(level));
        }
        return (sumOfVolumeAtPice / getTotalVolume());
    }

    public double standardDeviation() {
        double sumOfxMinusMean = 0;
        double mean = this.maxValue();

        for (double level : this.keySet()) {
            double levelMinusMean = (this.get(level) - mean);
            sumOfxMinusMean += (levelMinusMean * levelMinusMean);
        }

        return Math.sqrt(sumOfxMinusMean/getTotalVolume());
    }

    public double getTotalVolume() {
        double total = 0;
        for(double level : this.keySet()) {
            total += this.get(level);
        }
        return total;
    }

    public void addMissingValues() {
        if (this.size() > 1) {
            double first = this.firstKey().doubleValue();
            double last = this.lastKey().doubleValue();
            Set<Double> keys = this.keySet();
    
            for (double i = first; i < last; i+=0.5) {
                if (!keys.contains(i)) {
                    this.put(i, 0.0);
                }
            }
        }
    }

    public double maxValue() { 
        return this.values().stream().max(Double::compare).get();
    }

    public double minValue() { 
        return this.values().stream().min(Double::compare).get();
    }

    public double[] valuesToArray() {
        double[] x = this.values().stream().mapToDouble(value -> ((Number) value).doubleValue()).toArray();
        return x;
    }

    public double[] keysToArray() {
        double[] x = this.keySet().stream().mapToDouble(value -> ((Number) value).doubleValue()).toArray();
        return x;
    }
}
