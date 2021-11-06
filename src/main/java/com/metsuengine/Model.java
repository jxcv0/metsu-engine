package com.metsuengine;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * A market-making model based on Avellaneda & Stoikov
 * @link https://www.math.nyu.edu/~avellane/HighFrequencyTrading.pdf
 */
public class Model {
    
    private final TradeSeries tradeSeries;
    private final LimitOrderBook orderBook;
    private final DescriptiveStatistics stats;
    private final double riskAversionParam;
    private final double orderBookDensityParam;

    /**
     * The constructor
     * @param tradeSeries the tradeSeries
     * @param riskAversionParam the risk aversion parameter
     * @param orderBookDensityParam the liquidity / order book density parameter
     */
    public Model(TradeSeries tradeSeries, LimitOrderBook orderBook, double riskAversionParam, double orderBookDensityParam) {
        this.tradeSeries = tradeSeries;
        this.orderBook = orderBook;
        this.stats = new DescriptiveStatistics();
        this.riskAversionParam = riskAversionParam;
        this.orderBookDensityParam = orderBookDensityParam;
    }

    /**
     * Get std deviation of the prices within the tradeSeries as a proxy for volatility
     * @return the standard deviation
     */
    public double volatility() {
        stats.clear();
        tradeSeries.gettrades().forEach(t -> stats.addValue(t.price()));
        return stats.getStandardDeviation();
    }

    /**
     * Calculates reservation (mid price) 
     * @param marketMidprice the current market midprice
     * @param positionSize the current position size
     * @return the reservation price     
     */
    public double reservationPrice(double positionSize) {
        return  orderBook.midPrice() - positionSize * riskAversionParam * Math.pow(volatility(), 2);
    }

    public double halfSpread() {
        return (riskAversionParam * Math.pow(volatility(), 2) + ((2 / riskAversionParam) * Math.log(1 + (riskAversionParam / orderBookDensityParam)))) / 2 ;
    }

    public double askPrice(double positionSize) {
        return roundToTick(reservationPrice(positionSize) + halfSpread(), 0.5);
    }

    public double bidPrice(double positionSize) {
        return roundToTick(reservationPrice(positionSize) - halfSpread(), 0.5);
    }
    
    public double roundToTick(double num, double tick) {
        return Math.round(num / tick) * tick;
    }
}
