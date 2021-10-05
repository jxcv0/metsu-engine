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
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import org.ta4j.core.rules.OverIndicatorRule;
import org.ta4j.core.rules.TrailingStopLossRule;
import org.ta4j.core.rules.UnderIndicatorRule;

public class Strategies {

    public static Strategy basicEmaStrategy(BarSeries barSeries, int window) {
        ClosePriceIndicator close = new ClosePriceIndicator(barSeries);
        EMAIndicator ema = new EMAIndicator(close, window);
        EMAIndicator compoundEma = new EMAIndicator(ema, window);
        
        Num stop = DecimalNum.valueOf(0.);

        Rule entryRule = new CrossedUpIndicatorRule(ema, compoundEma);
        Rule exitRule = new TrailingStopLossRule(close, stop);

        return new BaseStrategy(entryRule, exitRule);
    }

    public static Strategy momentumStrategy(BarSeries barSeries) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(barSeries);

        EMAIndicator shortEma = new EMAIndicator(closePrice, 9);
        EMAIndicator longEma = new EMAIndicator(closePrice, 26);

        MACDIndicator macd = new MACDIndicator(closePrice, 9, 26);
        EMAIndicator emaMacd = new EMAIndicator(macd, 18);

        Rule entryRule = new OverIndicatorRule(shortEma, longEma) 
                .and(new OverIndicatorRule(macd, emaMacd)); 

        Rule exitRule = new UnderIndicatorRule(shortEma, longEma) 
                .and(new UnderIndicatorRule(macd, emaMacd)); 

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
