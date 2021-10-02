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
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.indicators.volume.VWAPIndicator;

public class SimpleBot {

    private static final Logger LOGGER = Logger.getLogger(SimpleBot.class.getName());

    public static void main(String[] args) {

        // int to = 1633175561;
        // int from = to - 1000000;
        // createKlineCSV(from, to);

        LOGGER.info("Getting kline data from CSV");
        CSVManager manager = new CSVManager("BTCUSD-inSampleKline.csv");
        BarSeries barSeries = manager.barSeriesFromCSV();
        BarSeriesManager barSeriesManager = new BarSeriesManager(barSeries);

        int window = 200;

        Strategy longStrategy = Strategies.standardDeviationLong(barSeries, window);
        TradingRecord longTradingRecord = barSeriesManager.run(longStrategy, Trade.TradeType.BUY);

        Strategy shortStrategy = Strategies.standardDeviationShort(barSeries, window);
        TradingRecord shortTradingRecord = barSeriesManager.run(shortStrategy, Trade.TradeType.SELL);
        
        LOGGER.info("Creating chart indicators");
        VWAPIndicator vwap = new VWAPIndicator(barSeries, window);
        StandardDeviationIndicator stdDev = new StandardDeviationIndicator(vwap, window);
        BollingerBandsMiddleIndicator middle = new BollingerBandsMiddleIndicator(vwap);
        BollingerBandsLowerIndicator lower = new BollingerBandsLowerIndicator(middle, stdDev);
        BollingerBandsUpperIndicator upper = new BollingerBandsUpperIndicator(middle, stdDev);
        
        LOGGER.info("Building Chart");
        TimeSeriesChart chart = new TimeSeriesChart("BTCUSD");
        chart.buildDataset("Close Price", barSeries);
        chart.buildDataset("Upper Deviation Band", barSeries, upper);
        chart.buildDataset("Moving Average", barSeries, middle);
        chart.buildDataset("Lower Deviation Band", barSeries, lower);
        chart.addMarkers(barSeries, longStrategy);
        chart.addMarkers(barSeries, shortStrategy);

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
