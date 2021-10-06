package com.metsuengine;

import java.util.logging.Logger;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.Trade.TradeType;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.MaximumDrawdownCriterion;
import org.ta4j.core.analysis.criteria.pnl.GrossReturnCriterion;
import org.ta4j.core.indicators.ATRIndicator;
import org.ta4j.core.indicators.ChandelierExitLongIndicator;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.keltner.KeltnerChannelLowerIndicator;
import org.ta4j.core.indicators.keltner.KeltnerChannelMiddleIndicator;
import org.ta4j.core.indicators.keltner.KeltnerChannelUpperIndicator;
import org.ta4j.core.indicators.pivotpoints.PivotPointIndicator;
import org.ta4j.core.indicators.pivotpoints.TimeLevel;

public class SimpleBot {

    private static final Logger LOGGER = Logger.getLogger(SimpleBot.class.getName());

    public static void main(String[] args) {

        // int from = (int) ZonedDateTime.now().minusMonths(1).toEpochSecond();
        // int to = (int) ZonedDateTime.now().toEpochSecond();
        // createKlineCSV(from, to);

        int window = 210;

        LOGGER.info("Getting kline data from CSV");
        CSVManager manager = new CSVManager("BTCUSD-03-10-21-minus1month.csv");
        BarSeries barSeries = manager.barSeriesFromCSV().getSubSeries(0, 5000);
        BarSeriesManager barSeriesManager = new BarSeriesManager(barSeries);

        TradingRecord longTradingRecord = barSeriesManager.run(Strategies.standardDeviationLong(barSeries, window), TradeType.BUY);

        LOGGER.info("Creating chart indicators");
        ClosePriceIndicator close = new ClosePriceIndicator(barSeries);
        EMAIndicator ema = new EMAIndicator(close, window);
        ChandelierExitLongIndicator trailingStop = new ChandelierExitLongIndicator(barSeries);
        PivotPointIndicator pivotPointIndicator = new PivotPointIndicator(barSeries, TimeLevel.DAY);

        LOGGER.info("Building Chart");
        TimeSeriesChart chart = new TimeSeriesChart("BTCUSD"); 
        chart.buildDataset("Close", barSeries);
        chart.buildDataset("pivotPointIndicator", barSeries, pivotPointIndicator);
        chart.buildDataset("trailingStop", barSeries, trailingStop);
        chart.buildDataset("ema", barSeries, ema);
        chart.displayChart();

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
