package com.metsuengine;

import java.util.ArrayList;
import java.util.List;
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
        this.addMissingValues();

    }

    public void createVolumeProfile(TradeSeries tradeSeries) {
        for (Trade trade : tradeSeries.getTrades()) {
            update(trade);
        }
    }

    public void update(Trade trade) {
        double price = trade.price();
        double size = trade.size();

        if(this.containsKey(price)) {
            double oldSize = this.get(price);
            this.put(price, oldSize + price);
        } else {
            this.put(price, size);
        }
    }

    public VolumeDistribution normalize() {
        VolumeDistribution normalizeDistribution = new VolumeDistribution();
        if (this.size() > 2) {
            double min = this.minValue();
            double max = this.maxValue();
            for (double level : this.keySet()) {
                normalizeDistribution.put(level, norm(this.get(level), min, max));
            }
        }
        return normalizeDistribution;
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

    public VolumeDistribution smooth() {
        VolumeDistribution smoothedDistribution = new VolumeDistribution();
        double[] oldKeys = this.keysToArray();
        double[] oldValues = this.valuesToArray();

        // TODO tune interpolator bandwidth
        LoessInterpolator interpolator = new LoessInterpolator(0.05, 2);
        double[] values = interpolator.smooth(oldKeys, oldValues);
        double[] keys = this.keysToArray();

        for (int i = 0; i < values.length; i++) {
            smoothedDistribution.put(keys[i], values[i]);
        }
        return smoothedDistribution;
    }

    @Deprecated
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
    
    public List<Double> highVolumeNodes() {
        VolumeDistribution volumeDistribution = this.smooth().normalize();
        List<Double> keys = new ArrayList<Double>();

        double first = volumeDistribution.firstKey();
        double last = volumeDistribution.lastKey();

        for (double level : volumeDistribution.keySet()) {
            if (level != first && level != last) {
                if (volumeDistribution.get(level) > volumeDistribution.get(level-0.5) && volumeDistribution.get(level) > volumeDistribution.get(level+0.5)){
                    keys.add(level);
                }
            }
        }
        return keys;
    }

    public List<Double> lowVolumeNodes() {
        VolumeDistribution volumeDistribution = this.smooth().normalize();
        List<Double> keys = new ArrayList<Double>();

        double first = volumeDistribution.firstKey();
        double last = volumeDistribution.lastKey();

        for (double level : volumeDistribution.keySet()) {
            if (level != first && level != last) {
                if (volumeDistribution.get(level) < volumeDistribution.get(level-0.5) && volumeDistribution.get(level) < volumeDistribution.get(level+0.5)){
                    keys.add(level);
                }
            }
        }
        return keys;
    }

    public double maxValue() { 
        return this.values().stream().max(Double::compare).get();
    }

    public double minValue() { 
        return this.values().stream().min(Double::compare).get();
    }

    public double maxKey() { 
        return this.keySet().stream().max(Double::compare).get();
    }

    public double minKey() { 
        return this.keySet().stream().min(Double::compare).get();
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
