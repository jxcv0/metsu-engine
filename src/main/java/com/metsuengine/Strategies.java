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

    public static Strategy momentumStrategyLong(BarSeries barSeries, int window) {
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(barSeries);
        EMAIndicator shortEma = new EMAIndicator(closePriceIndicator, window);
        EMAIndicator longEma = new EMAIndicator(closePriceIndicator, window*2);

        Rule entryRule = new CrossedUpIndicatorRule(shortEma, longEma);
        Rule exitRule = new CrossedDownIndicatorRule(shortEma, longEma);

        return new BaseStrategy(entryRule, exitRule);
    }

    public static Strategy momentumStrategyShort(BarSeries barSeries, int window) {
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(barSeries);
        EMAIndicator shortEma = new EMAIndicator(closePriceIndicator, window);
        EMAIndicator longEma = new EMAIndicator(closePriceIndicator, window*2);

        Rule entryRule = new CrossedDownIndicatorRule(shortEma, longEma);
        Rule exitRule = new CrossedUpIndicatorRule(shortEma, longEma);

        return new BaseStrategy(entryRule, exitRule);
    }
    
    public static Strategy standardDeviationLong(BarSeries barSeries, int window) {

        ClosePriceIndicator close = new ClosePriceIndicator(barSeries);
        EMAIndicator ema = new EMAIndicator(close, window);
        StandardDeviationIndicator stdDev = new StandardDeviationIndicator(close, window);
        BollingerBandsMiddleIndicator middle = new BollingerBandsMiddleIndicator(ema);
        BollingerBandsLowerIndicator lower = new BollingerBandsLowerIndicator(middle, stdDev);

        Rule entryRule = new CrossedDownIndicatorRule(close, lower);
        Rule exitRule = new CrossedUpIndicatorRule(close, middle);

        return new BaseStrategy(entryRule, exitRule);
    }

    public static Strategy standardDeviationShort(BarSeries barSeries, int window) {

        ClosePriceIndicator close = new ClosePriceIndicator(barSeries);
        EMAIndicator ema = new EMAIndicator(close, window);        StandardDeviationIndicator stdDev = new StandardDeviationIndicator(close, window);
        BollingerBandsMiddleIndicator middle = new BollingerBandsMiddleIndicator(ema);
        BollingerBandsUpperIndicator upper = new BollingerBandsUpperIndicator(middle, stdDev);

        Rule entryRule = new CrossedUpIndicatorRule(close, upper);
        Rule exitRule = new CrossedDownIndicatorRule(close, middle);

        return new BaseStrategy(entryRule, exitRule);
    }
}
