package com.metsuengine;

import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

public class Strategies {
    
    public static Strategy standardDeviationLong(BarSeries barSeries, int window) {

        ClosePriceIndicator close = new ClosePriceIndicator(barSeries);
        SMAIndicator sma = new SMAIndicator(close, window);
        StandardDeviationIndicator stdDev = new StandardDeviationIndicator(close, window);
        BollingerBandsMiddleIndicator middle = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsLowerIndicator lower = new BollingerBandsLowerIndicator(middle, stdDev);

        Rule entryRule = new CrossedDownIndicatorRule(close, lower);
        Rule exitRule = new CrossedUpIndicatorRule(close, middle);

        Strategy strategy = new BaseStrategy("SD", entryRule, exitRule, 100);
        return strategy;
    }

    public static Strategy standardDeviationShort(BarSeries barSeries, int window) {

        ClosePriceIndicator close = new ClosePriceIndicator(barSeries);
        SMAIndicator sma = new SMAIndicator(close, window);
        StandardDeviationIndicator stdDev = new StandardDeviationIndicator(close, window);
        BollingerBandsMiddleIndicator middle = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsUpperIndicator upper = new BollingerBandsUpperIndicator(middle, stdDev);

        Rule entryRule = new CrossedUpIndicatorRule(close, upper);
        Rule exitRule = new CrossedDownIndicatorRule(close, middle);

        Strategy strategy = new BaseStrategy("SD", entryRule, exitRule, 100);
        strategy.setUnstablePeriod(100);
        return strategy;
    }

    public static Strategy momentumHedgeLong(BarSeries barSeries, int window) {
        ClosePriceIndicator close = new ClosePriceIndicator(barSeries);
        EMAIndicator emaShort = new EMAIndicator(close, window);
        EMAIndicator emaLong = new EMAIndicator(close, window * 2);

        Rule entryRule = new CrossedUpIndicatorRule( emaShort, emaLong);
        Rule exitRule = new CrossedDownIndicatorRule(emaShort, emaLong);

        return new BaseStrategy("MH", entryRule, exitRule);
    }

    public static Strategy momentumHedgeShort(BarSeries barSeries, int window) {
        ClosePriceIndicator close = new ClosePriceIndicator(barSeries);
        EMAIndicator emaShort = new EMAIndicator(close, window);
        EMAIndicator emaLong = new EMAIndicator(close, window * 2);

        Rule entryRule = new CrossedDownIndicatorRule(emaShort, emaLong);
        Rule exitRule = new CrossedUpIndicatorRule(emaShort, emaLong);

        return new BaseStrategy("MH", entryRule, exitRule);
    }
}