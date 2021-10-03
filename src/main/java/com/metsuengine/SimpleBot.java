package com.metsuengine;

import java.util.logging.Logger;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.Strategy;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.MaximumDrawdownCriterion;
import org.ta4j.core.analysis.criteria.pnl.GrossReturnCriterion;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;

public class SimpleBot {

    private static final Logger LOGGER = Logger.getLogger(SimpleBot.class.getName());

    public static void main(String[] args) {

        // int to = 1600000000-30000;
        // int from = to - 60000;
        // createKlineCSV(from, to);

        LOGGER.info("Getting kline data from CSV");
        CSVManager manager = new CSVManager("BTCUSD-inSampleKline-1.csv");
        BarSeries barSeries = manager.barSeriesFromCSV();
        BarSeriesManager barSeriesManager = new BarSeriesManager(barSeries);

        int window = 40;

        Strategy longStrategy = Strategies.momentumStrategyLong(barSeries, window);
        Strategy shortStrategy = Strategies.momentumStrategyShort(barSeries, window);
        TradingRecord longTradingRecord = barSeriesManager.run(longStrategy, Trade.TradeType.BUY);
        TradingRecord shortTradingRecord = barSeriesManager.run(shortStrategy, Trade.TradeType.SELL);
        
        LOGGER.info("Creating chart indicators");
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(barSeries);
        EMAIndicator shortEma = new EMAIndicator(closePriceIndicator, window);
        EMAIndicator longEma = new EMAIndicator(closePriceIndicator, window*2);
        
        LOGGER.info("Building Chart");
        TimeSeriesChart chart = new TimeSeriesChart("BTCUSD");
        chart.buildDataset("Close Price", barSeries);
        chart.buildDataset("Short Ema", barSeries, shortEma);
        chart.buildDataset("Long Ema", barSeries, longEma);
        chart.addMarkers(barSeries, longStrategy);

        chart.displayChart();

        AnalysisCriterion drawdownCriterion = new MaximumDrawdownCriterion();
        AnalysisCriterion returnCriterion = new GrossReturnCriterion();

        double longDrawdown = drawdownCriterion.calculate(barSeries, longTradingRecord).doubleValue();
        double longReturn = returnCriterion.calculate(barSeries, longTradingRecord).doubleValue();
        double shortDrawdown = drawdownCriterion.calculate(barSeries, shortTradingRecord).doubleValue();
        double shortReturn = returnCriterion.calculate(barSeries, shortTradingRecord).doubleValue();
        
        System.out.println("Long Drawdown: " + longDrawdown * 100);
        System.out.println("Long Return: " + longReturn * 100);
        System.out.println("Short Drawdown: " + shortDrawdown * 100);
        System.out.println("Short Return: " + shortReturn * 100);
    }

    public static void createKlineCSV(int from, int to) {
        BybitEndpoint endpoint = new BybitEndpoint("BTCUSD");

        LOGGER.info("Getting kline data");
        for (int i = from; i < to; i+=60) {
            endpoint.getKlineRecords(i);
        }
    }
}
