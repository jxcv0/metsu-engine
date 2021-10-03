package com.metsuengine;

import java.util.logging.Logger;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.Trade.TradeType;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.MaximumDrawdownCriterion;
import org.ta4j.core.analysis.criteria.pnl.GrossReturnCriterion;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

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

        TradingRecord longTradingRecord = barSeriesManager.run(Strategies.momentumStrategy(barSeries), TradeType.BUY);

        LOGGER.info("Creating chart indicators");
        ClosePriceIndicator closePrice = new ClosePriceIndicator(barSeries);
        EMAIndicator shortEma = new EMAIndicator(closePrice, 9);
        EMAIndicator longEma = new EMAIndicator(closePrice, 26);
        MACDIndicator macd = new MACDIndicator(closePrice, 9, 26);
        EMAIndicator emaMacd = new EMAIndicator(macd, 18);
 
        
        LOGGER.info("Building Chart");
        TimeSeriesChart chart = new TimeSeriesChart("BTCUSD");
        chart.buildDataset("close", barSeries);
        chart.buildDataset("shortEma", barSeries, shortEma);
        chart.buildDataset("longEma", barSeries, longEma);
        chart.addMarkers(barSeries, Strategies.momentumStrategy(barSeries));
        chart.displayChart();

        TimeSeriesChart indicatorChart = new TimeSeriesChart("Other Indicators");
        indicatorChart.buildDataset("macd", barSeries, macd);
        indicatorChart.buildDataset("emaMacd", barSeries, emaMacd);
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
