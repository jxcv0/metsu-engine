package com.metsuengine;

import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.indicators.volume.VWAPIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

public class Strategies {

    public static Strategy momentumStrategy(BarSeries barSeries) {
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(barSeries);
        SMAIndicator sma = new SMAIndicator(closePriceIndicator, 40);

        Rule entryRule = new CrossedUpIndicatorRule(closePriceIndicator, sma);
        Rule exitRule = new CrossedDownIndicatorRule(closePriceIndicator, sma);

        return new BaseStrategy(entryRule, exitRule);
    }
    
    public static Strategy bollingerBandsStrategyLong(BarSeries barSeries) {

        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(barSeries);
        VWAPIndicator vwap = new VWAPIndicator(barSeries, 100);
        StandardDeviationIndicator stdDev = new StandardDeviationIndicator(vwap, 100);
        BollingerBandsMiddleIndicator middleIndicator = new BollingerBandsMiddleIndicator(vwap);
        BollingerBandsLowerIndicator lowerIndicator = new BollingerBandsLowerIndicator(middleIndicator, stdDev);

        Rule entryRule = new CrossedDownIndicatorRule(closePriceIndicator, lowerIndicator);
        Rule exitRule = new CrossedUpIndicatorRule(closePriceIndicator, middleIndicator);

        return new BaseStrategy(entryRule, exitRule);
    }
}
