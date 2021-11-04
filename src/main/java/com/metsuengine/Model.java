package com.metsuengine;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * A market-making model based on Avellaneda & Stoikov
 * @link https://www.math.nyu.edu/~avellane/HighFrequencyTrading.pdf
 */
public class Model {
    
    private final TradeSeries tickSeries;
    private final DescriptiveStatistics stats;
    private final double riskAversionParam;
    private final double orderBookDensityParam;

    /**
     * The constructor
     * @param tickSeries the TickSeries
     * @param riskAversionParam the risk aversion parameter
     * @param orderBookDensityParam the liquidity / order book density parameter
     */
    public Model(TradeSeries tickSeries, double riskAversionParam, double orderBookDensityParam) {
        this.tickSeries = tickSeries;
        this.stats = new DescriptiveStatistics();
        this.riskAversionParam = riskAversionParam;
        this.orderBookDensityParam = orderBookDensityParam;
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

    /**
     * Calculates reservation (mid price) 
     * @param marketMidprice the current market midprice
     * @param positionSize the current position size
     * @return the reservation price     
     */
    public double reservationPrice(double marketMidprice, double positionSize) {
        return marketMidprice - positionSize * riskAversionParam * Math.pow(volatility(), 2);
    }

    public double halfSpread() {
        return (riskAversionParam * Math.pow(volatility(), 2) + ((2 / riskAversionParam) * Math.log(1 + (riskAversionParam / orderBookDensityParam)))) / 2 ;
    }

    public double askPrice(double marketMidprice, double positionSize) {
        return reservationPrice(marketMidprice, positionSize) + halfSpread();
    }

    public double bidPrice(double marketMidprice, double positionSize) {
        return reservationPrice(marketMidprice, positionSize) - halfSpread();
    }
}
