package com.metsuengine;

import java.util.logging.Logger;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.Trade.TradeType;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.MaximumDrawdownCriterion;
import org.ta4j.core.analysis.criteria.pnl.GrossReturnCriterion;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;

public class SimpleBot {

    private static final Logger LOGGER = Logger.getLogger(SimpleBot.class.getName());

    public static void main(String[] args) {

        // int from = (int) ZonedDateTime.now().minusMonths(1).toEpochSecond();
        // int to = (int) ZonedDateTime.now().toEpochSecond();
        // createKlineCSV(from, to);

        LOGGER.info("Getting kline data from CSV");
        CSVManager manager = new CSVManager("BTCUSD-03-10-21-minus1month.csv");
        BarSeries barSeries = manager.barSeriesFromCSV();
        BarSeriesManager barSeriesManager = new BarSeriesManager(barSeries);

        int window = 100;

        TradingRecord longTradingRecord = barSeriesManager.run(
            Strategies.standardDeviationLong(barSeries.getSubSeries(0, 1000), window), TradeType.BUY
            );

        LOGGER.info("Creating chart indicators");
        ClosePriceIndicator close = new ClosePriceIndicator(barSeries);
        SMAIndicator sma = new SMAIndicator(close, window);
        StandardDeviationIndicator stdDev = new StandardDeviationIndicator(close, window);
        BollingerBandsMiddleIndicator middle = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsUpperIndicator upper = new BollingerBandsUpperIndicator(middle, stdDev);
        BollingerBandsLowerIndicator lower = new BollingerBandsLowerIndicator(middle, stdDev);
        
        
        LOGGER.info("Building Chart");
        TimeSeriesChart chart = new TimeSeriesChart("BTCUSD");
        chart.buildDataset("Close", barSeries);
        chart.buildDataset("SMA", barSeries, middle);
        chart.buildDataset("Upper Band", barSeries, upper);
        chart.buildDataset("Lower Band", barSeries, lower);

        TimeSeriesChart indicatorChart = new TimeSeriesChart("Other Indicators");
                
        indicatorChart.displayChart();

        AnalysisCriterion drawdownCriterion = new MaximumDrawdownCriterion();
        AnalysisCriterion returnCriterion = new GrossReturnCriterion();

        double longDrawdown = drawdownCriterion.calculate(barSeries, longTradingRecord).doubleValue();
        double longReturn = returnCriterion.calculate(barSeries, longTradingRecord).doubleValue();

        System.out.println("Number of positions: " + longTradingRecord.getPositionCount());        
        System.out.println("Long Drawdown: " + longDrawdown * 100);
        System.out.println("Long Return: " + longReturn * 100);
    }

    public static void createKlineCSV(int from, int to) {
        BybitEndpoint endpoint = new BybitEndpoint("BTCUSD");

        LOGGER.info("Getting kline data");
        for (int i = from; i < to; i+=60) {
            endpoint.getKlineRecords(i);
        }
    }
}
