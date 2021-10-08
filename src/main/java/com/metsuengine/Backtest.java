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
import org.ta4j.core.indicators.helpers.DifferenceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;

public class Backtest {

    private static final Logger LOGGER = Logger.getLogger(Backtest.class.getName());

    public static void main(String[] args) {

        // int from = (int) ZonedDateTime.now().minusMonths(1).toEpochSecond();
        // int to = (int) ZonedDateTime.now().toEpochSecond();
        // createPairsKlineCSV(from, to);

        LOGGER.info("Getting kline data from CSV");
        CSVManager btcManager = new CSVManager("BTCUSD08-10.csv");
        CSVManager xrpManager = new CSVManager("XRPUSD08-10.csv");
        BarSeries btcBarSeries = btcManager.barSeriesFromCSV();
        BarSeries xrpBarSeries = xrpManager.barSeriesFromCSV();

        int window = 220;

        DifferenceIndicator differenceIndicator = new DifferenceIndicator(
            new ClosePriceIndicator(btcBarSeries), new ClosePriceIndicator(xrpBarSeries));
        
        SMAIndicator sma = new SMAIndicator(differenceIndicator, window);
        StandardDeviationIndicator deviation = new StandardDeviationIndicator(differenceIndicator, window*2);
        BollingerBandsMiddleIndicator bbm = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsLowerIndicator bbl = new BollingerBandsLowerIndicator(bbm, deviation);
        BollingerBandsUpperIndicator bbu = new BollingerBandsUpperIndicator(bbm, deviation);
        
        BarSeriesManager btcSeriesManager = new BarSeriesManager(btcBarSeries);
        BarSeriesManager xrpSeriesManager = new BarSeriesManager(xrpBarSeries);
        TradingRecord xrpShortRecord = xrpSeriesManager.run(Strategies.meanReversionShort(btcBarSeries, xrpBarSeries, window), TradeType.SELL);
        TradingRecord btcLongRecord = btcSeriesManager.run(Strategies.meanReversionLong(btcBarSeries, xrpBarSeries, window), TradeType.BUY);

        LOGGER.info("Building Chart");
        TimeSeriesChart chart = new TimeSeriesChart("BTCUSD");
        chart.buildDataset("Spread", btcBarSeries, differenceIndicator);
        chart.buildDataset("SMA", btcBarSeries, sma);
        chart.buildDataset("BBL", btcBarSeries, bbl);
        chart.buildDataset("BBU", btcBarSeries, bbu);
        chart.addMarkers(btcBarSeries, Strategies.meanReversionShort(btcBarSeries, xrpBarSeries, window), TradeType.SELL);
        chart.addMarkers(xrpBarSeries, Strategies.meanReversionLong(btcBarSeries, xrpBarSeries, window), TradeType.BUY);
        chart.displayChart();

        AnalysisCriterion maxDrawdown = new MaximumDrawdownCriterion();
        AnalysisCriterion grossReturn = new GrossReturnCriterion();

        System.out.println("Long Maximum Drawdown: " + maxDrawdown.calculate(btcBarSeries, btcLongRecord));
        System.out.println("Short Maximum Drawdown: " + maxDrawdown.calculate(xrpBarSeries, xrpShortRecord));
        System.out.println("Long Returns : " + grossReturn.calculate(btcBarSeries, btcLongRecord));
        System.out.println("Short Returns: " + grossReturn.calculate(xrpBarSeries, xrpShortRecord));
    }

    public static void createKlineCSV(int from, int to) {
        BybitEndpoint endpoint = new BybitEndpoint("BTCUSD");

        LOGGER.info("Getting kline data");
        for (int i = from; i < to; i+=60) {
            endpoint.getKlineRecords(i);
        }
    }

    public static void createPairsKlineCSV(int from, int to) {
        BybitEndpoint firstEndpoint = new BybitEndpoint("BTCUSD");
        BybitEndpoint secondEndpoint = new BybitEndpoint("XRPUSD");
        LOGGER.info("Getting kline data");
        for (int i = from; i < to; i+=60) {
            firstEndpoint.getKlineRecords(i);
            secondEndpoint.getKlineRecords(i);
        }
    }
}
