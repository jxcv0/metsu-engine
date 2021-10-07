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
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;

public class Backtest {

    private static final Logger LOGGER = Logger.getLogger(Backtest.class.getName());

    public static void main(String[] args) {

        // int from = (int) ZonedDateTime.now().minusMonths(1).toEpochSecond();
        // int to = (int) ZonedDateTime.now().toEpochSecond();
        // createKlineCSV(from, to);

        LOGGER.info("Getting kline data from CSV");
        CSVManager manager = new CSVManager("BTCUSD-03-10-21-minus1month.csv");
        BarSeries barSeries = manager.barSeriesFromCSV().getSubSeries(0, 10000);
        BarSeriesManager barSeriesManager = new BarSeriesManager(barSeries);

        int window = 600;

        TradingRecord longSDTradingRecord = barSeriesManager.run(Strategies.standardDeviationLong(barSeries, window), TradeType.BUY);
        TradingRecord shortSDTradingRecord = barSeriesManager.run(Strategies.standardDeviationShort(barSeries, window), TradeType.SELL);
        TradingRecord longMHTradingRecord = barSeriesManager.run(Strategies.momentumHedgeLong(barSeries, window), TradeType.BUY);
        TradingRecord shortMHTradingRecord = barSeriesManager.run(Strategies.momentumHedgeShort(barSeries, window), TradeType.SELL);

        LOGGER.info("Creating chart indicators");
        ClosePriceIndicator close = new ClosePriceIndicator(barSeries);
        SMAIndicator sma = new SMAIndicator(close, window);
        StandardDeviationIndicator sd = new StandardDeviationIndicator(close, window);
        BollingerBandsMiddleIndicator bbm = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsLowerIndicator bbl = new BollingerBandsLowerIndicator(bbm, sd);
        BollingerBandsUpperIndicator bbu = new BollingerBandsUpperIndicator(bbm, sd);
        EMAIndicator emaShort = new EMAIndicator(close, window);
        EMAIndicator emaLong = new EMAIndicator(close, window * 2);

        LOGGER.info("Building Chart");
        TimeSeriesChart chart = new TimeSeriesChart("BTCUSD");
        chart.buildDataset("Close", barSeries);
        chart.buildDataset("BBM", barSeries, bbm);
        chart.buildDataset("BBL", barSeries, bbl);
        chart.buildDataset("BBU", barSeries, bbu);
        chart.buildDataset("Short EMA", barSeries, emaShort);
        chart.buildDataset("Long EMA", barSeries, emaLong);
        chart.addMarkers(barSeries, Strategies.standardDeviationLong(barSeries, window), TradeType.BUY);
        chart.addMarkers(barSeries, Strategies.standardDeviationShort(barSeries, window), TradeType.SELL);
        chart.addMarkers(barSeries, Strategies.momentumHedgeLong(barSeries, window), TradeType.BUY);
        chart.addMarkers(barSeries, Strategies.momentumHedgeShort(barSeries, window), TradeType.SELL);
        chart.displayChart();

        AnalysisCriterion maxDrawdownCriterion = new MaximumDrawdownCriterion();
        AnalysisCriterion returnCriterion = new GrossReturnCriterion();

        double longDrawdown = maxDrawdownCriterion.calculate(barSeries, longSDTradingRecord).doubleValue();
        double longReturn = returnCriterion.calculate(barSeries, longSDTradingRecord).doubleValue();
        double shortDrawdown = maxDrawdownCriterion.calculate(barSeries, shortSDTradingRecord).doubleValue();
        double shortReturn = returnCriterion.calculate(barSeries, shortSDTradingRecord).doubleValue();
        double longHedgeDrawdown = maxDrawdownCriterion.calculate(barSeries, longMHTradingRecord).doubleValue();
        double longHedgeReturn = returnCriterion.calculate(barSeries, longMHTradingRecord).doubleValue();
        double shortHedgeDrawdown = maxDrawdownCriterion.calculate(barSeries, shortMHTradingRecord).doubleValue();
        double shortHedgeReturn = returnCriterion.calculate(barSeries, shortMHTradingRecord).doubleValue();

        System.out.println("Number of Long positions: " + longSDTradingRecord.getPositionCount());
        System.out.println("Number of Short positions: " + shortSDTradingRecord.getPositionCount());
        System.out.println("Long Drawdown: " + longDrawdown * 100);
        System.out.println("Long Return: " + longReturn * 100);
        System.out.println("Short Drawdown: " + shortDrawdown * 100);
        System.out.println("Short Return: " + shortReturn * 100);
        System.out.println("Number of Long positions: " + longMHTradingRecord.getPositionCount());
        System.out.println("Number of Short positions: " + shortMHTradingRecord.getPositionCount());
        System.out.println("Hedge Long Drawdown: " + longHedgeDrawdown * 100);
        System.out.println("Hedge Long Return: " + longHedgeReturn * 100);
        System.out.println("Hedge Short Drawdown: " + shortHedgeDrawdown * 100);
        System.out.println("Hedge Short Return: " + shortHedgeReturn * 100);
    }

    public static void createKlineCSV(int from, int to) {
        BybitEndpoint endpoint = new BybitEndpoint("BTCUSD");

        LOGGER.info("Getting kline data");
        for (int i = from; i < to; i+=60) {
            endpoint.getKlineRecords(i);
        }
    }
}
