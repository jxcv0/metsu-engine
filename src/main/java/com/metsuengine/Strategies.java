package com.metsuengine;

import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import org.ta4j.core.rules.OverIndicatorRule;
import org.ta4j.core.rules.UnderIndicatorRule;

public class Strategies {

    public static Strategy momentumStrategy(BarSeries barSeries) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(barSeries);

        // The bias is bullish when the shorter-moving average moves above the longer
        // moving average.
        // The bias is bearish when the shorter-moving average moves below the longer
        // moving average.
        EMAIndicator shortEma = new EMAIndicator(closePrice, 9);
        EMAIndicator longEma = new EMAIndicator(closePrice, 26);

        MACDIndicator macd = new MACDIndicator(closePrice, 9, 26);
        EMAIndicator emaMacd = new EMAIndicator(macd, 18);

        // Entry rule
        Rule entryRule = new OverIndicatorRule(shortEma, longEma) // Trend
                .and(new OverIndicatorRule(macd, emaMacd)); // Signal 2

        // Exit rule
        Rule exitRule = new UnderIndicatorRule(shortEma, longEma) // Trend
                .and(new UnderIndicatorRule(macd, emaMacd)); // Signal 2

        return new BaseStrategy(entryRule, exitRule);
    }
    
    public static Strategy standardDeviationLong(BarSeries barSeries, int window) {

        ClosePriceIndicator close = new ClosePriceIndicator(barSeries);
        SMAIndicator sma = new SMAIndicator(close, window);
        StandardDeviationIndicator stdDev = new StandardDeviationIndicator(close, window);
        BollingerBandsMiddleIndicator middle = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsLowerIndicator lower = new BollingerBandsLowerIndicator(middle, stdDev);

        Rule entryRule = new CrossedDownIndicatorRule(close, lower);
        Rule exitRule = new CrossedUpIndicatorRule(close, middle);

        return new BaseStrategy(entryRule, exitRule);
    }

    public static Strategy standardDeviationShort(BarSeries barSeries, int window) {

        ClosePriceIndicator close = new ClosePriceIndicator(barSeries);
        SMAIndicator sma = new SMAIndicator(close, window);
        StandardDeviationIndicator stdDev = new StandardDeviationIndicator(close, window);
        BollingerBandsMiddleIndicator middle = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsUpperIndicator upper = new BollingerBandsUpperIndicator(middle, stdDev);

        Rule entryRule = new CrossedUpIndicatorRule(close, upper);
        Rule exitRule = new CrossedDownIndicatorRule(close, middle);

        return new BaseStrategy(entryRule, exitRule);
    }
}
