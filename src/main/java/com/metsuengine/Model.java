package com.metsuengine;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * A market-making model based on Avellaneda & Stoikov
 * @link https://www.math.nyu.edu/~avellane/HighFrequencyTrading.pdf
 */
public class Model {
    
    private final TickSeries tickSeries;
    private final DescriptiveStatistics stats;

    /**
     * The constructor
     * @param tickSeries the TickSeries
     */
    public Model(TickSeries tickSeries) {
        this.tickSeries = tickSeries;
        this.stats = new DescriptiveStatistics();
    }

    /**
     * Get std deviation of the prices within the TickSeries as a proxy for volatility
     * @return the standard deviation
     */
    public double volatility() {
        stats.clear();
        tickSeries.getTicks().forEach(t -> stats.addValue(t.price()));
        return stats.getStandardDeviation();
    }
}
