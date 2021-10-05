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
        BarSeries barSeries = manager.barSeriesFromCSV().getSubSeries(0, 1000);
        // BarSeriesManager barSeriesManager = new BarSeriesManager(barSeries);

        // TradingRecord longTradingRecord = barSeriesManager.run(
        //     Strategies.standardDeviationLong(barSeries.getSubSeries(0, 1000), window), TradeType.BUY
        //     );

        LOGGER.info("Creating chart indicators");
        GaussianIndicator gaussianIndicator = new GaussianIndicator(barSeries, 13, 8);
        
        LOGGER.info("Building Chart");
        TimeSeriesChart chart = new TimeSeriesChart("BTCUSD");
        chart.buildDataset("Close", barSeries);
        chart.displayChart();

        TimeSeriesChart indicatorChart = new TimeSeriesChart("Indicator Chart");
        indicatorChart.buildDataset("Gaussian Filter", barSeries, gaussianIndicator);
        indicatorChart.displayChart();

        // AnalysisCriterion drawdownCriterion = new MaximumDrawdownCriterion();
        // AnalysisCriterion returnCriterion = new GrossReturnCriterion();

        // double longDrawdown = drawdownCriterion.calculate(barSeries, longTradingRecord).doubleValue();
        // double longReturn = returnCriterion.calculate(barSeries, longTradingRecord).doubleValue();

        // System.out.println("Number of positions: " + longTradingRecord.getPositionCount());        
        // System.out.println("Long Drawdown: " + longDrawdown * 100);
        // System.out.println("Long Return: " + longReturn * 100);
    }

    public static void createKlineCSV(int from, int to) {
        BybitEndpoint endpoint = new BybitEndpoint("BTCUSD");

        LOGGER.info("Getting kline data");
        for (int i = from; i < to; i+=60) {
            endpoint.getKlineRecords(i);
        }
    }
}
