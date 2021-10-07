package com.metsuengine;

import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.DistanceFromMAIndicator;
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

        Strategy strategy = new BaseStrategy(entryRule, exitRule);
        strategy.setUnstablePeriod(100);
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

        Strategy strategy = new BaseStrategy(entryRule, exitRule);
        strategy.setUnstablePeriod(100);
        return strategy;
    }

    public static Strategy momentumHedgeLong(BarSeries barSeries, int window) {
        ClosePriceIndicator close = new ClosePriceIndicator(barSeries);
        EMAIndicator ema = new EMAIndicator(close, window * 3);
        DistanceFromMAIndicator distance = new DistanceFromMAIndicator(barSeries, ema);

        Rule entryRule = new CrossedUpIndicatorRule(close, ema);
        Rule exitRule = new CrossedDownIndicatorRule(close, ema);

        return new BaseStrategy(entryRule, exitRule);
    }
}
