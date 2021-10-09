package com.metsuengine;

import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.DifferenceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

public class Strategies {
    
    public static Strategy pairsStrategyShort(BarSeries first, BarSeries second, int window) {

        DifferenceIndicator differenceIndicator = new DifferenceIndicator(
            new ClosePriceIndicator(first), new ClosePriceIndicator(second));

        EMAIndicator ema = new EMAIndicator(differenceIndicator, window);
        StandardDeviationIndicator stdDev = new StandardDeviationIndicator(differenceIndicator, window*2);
        BollingerBandsMiddleIndicator bbm = new BollingerBandsMiddleIndicator(ema);
        BollingerBandsLowerIndicator bbl = new BollingerBandsLowerIndicator(bbm, stdDev);

        Rule entryRule = new CrossedDownIndicatorRule(differenceIndicator, bbl);
        Rule exitRule = new CrossedUpIndicatorRule(differenceIndicator, bbm);

        Strategy strategy = new BaseStrategy("SD", entryRule, exitRule, 100);
        return strategy;
    }
}