package com.metsuengine;

import java.text.DecimalFormat;
import java.util.logging.Logger;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Strategy;
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
import org.ta4j.core.indicators.statistics.SigmaIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

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

        int window = 1200;
        
        ClosePriceIndicator btcClose = new ClosePriceIndicator(btcBarSeries);
        ClosePriceIndicator xrpClose = new ClosePriceIndicator(xrpBarSeries); 
        SigmaIndicator btcSigma = new SigmaIndicator(btcClose, window);
        SigmaIndicator xrpSigma = new SigmaIndicator(xrpClose, window);
        DifferenceIndicator pair = new DifferenceIndicator(btcSigma, xrpSigma);
        SMAIndicator sma = new SMAIndicator(pair, window);
        StandardDeviationIndicator deviation = new StandardDeviationIndicator(pair, window);
        BollingerBandsMiddleIndicator bbm = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsLowerIndicator bbl = new BollingerBandsLowerIndicator(bbm, deviation);
        BollingerBandsUpperIndicator bbu = new BollingerBandsUpperIndicator(bbm, deviation);
        
        Strategy btcShort = new BaseStrategy("BTC",  new CrossedUpIndicatorRule(pair, bbu), new CrossedDownIndicatorRule(pair, bbm));
        Strategy xrpShort = new BaseStrategy("XRP",  new CrossedDownIndicatorRule(pair, bbl), new CrossedUpIndicatorRule(pair, bbm));

        BarSeriesManager btcSeriesManager = new BarSeriesManager(btcBarSeries);
        BarSeriesManager xrpSeriesManager = new BarSeriesManager(xrpBarSeries);
        TradingRecord btcShortRecord = btcSeriesManager.run(btcShort, TradeType.SELL);
        TradingRecord xrpShortRecord = xrpSeriesManager.run(xrpShort, TradeType.SELL);

        LOGGER.info("Building Charts");
        TimeSeriesChart pairChart = new TimeSeriesChart("Difference");
        pairChart.buildDataset("Pair Delta", btcBarSeries, pair);
        pairChart.buildDataset("BBM", btcBarSeries, bbm);
        pairChart.buildDataset("BBL", btcBarSeries, bbl);
        pairChart.buildDataset("BBU", btcBarSeries, bbu);
        pairChart.displayChart();

        TimeSeriesChart sigmaChart = new TimeSeriesChart("Sigma Delta");
        sigmaChart.buildDataset("BTCUSD", btcBarSeries, btcSigma);
        sigmaChart.buildDataset("XRPUSD", xrpBarSeries, xrpSigma);
        sigmaChart.addMarkers(btcBarSeries, btcShortRecord);
        sigmaChart.displayChart();

        AnalysisCriterion maxDrawdown = new MaximumDrawdownCriterion();
        AnalysisCriterion grossReturn = new GrossReturnCriterion();
        
        DecimalFormat formatter = new DecimalFormat("###.##");
        System.out.println("Number of BTC Trades: " + btcShortRecord.getPositionCount());
        System.out.println("Number of XRP Trades: " + xrpShortRecord.getPositionCount());
        System.out.println("BTC Maximum Short Drawdown: " + formatter.format(maxDrawdown.calculate(btcBarSeries, btcShortRecord).doubleValue() * 100));
        System.out.println("XRP Maximum Short Drawdown: " + formatter.format(maxDrawdown.calculate(xrpBarSeries, xrpShortRecord).doubleValue() * 100));
        System.out.println("BTC Short Returns : " + formatter.format(grossReturn.calculate(btcBarSeries, btcShortRecord).doubleValue() * 100));
        System.out.println("XRP Short Returns: " + formatter.format(grossReturn.calculate(xrpBarSeries, xrpShortRecord).doubleValue() * 100));
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
