package com.metsuengine;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Strategy;
import org.ta4j.core.Trade.TradeType;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.LinearTransactionCostCriterion;
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

    public static void NOT(String[] args) {
        int from = (int) ZonedDateTime.now().minusMonths(1).toEpochSecond();
        int to = (int) ZonedDateTime.now().toEpochSecond();
        createPairsKlineCSV(from, to);
    }

    public static void main(String[] args) {

        LOGGER.info("Getting kline data from CSV");
        CSVManager btcManager = new CSVManager("BTCUSD09-10.csv");
        CSVManager xrpManager = new CSVManager("XRPUSD09-10.csv");
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
        Strategy btcLong = new BaseStrategy("BTC", new CrossedDownIndicatorRule(pair, bbl), new CrossedUpIndicatorRule(pair, bbm));
        Strategy xrpLong = new BaseStrategy("BTC",  new CrossedUpIndicatorRule(pair, bbu), new CrossedDownIndicatorRule(pair, bbm));

        BarSeriesManager btcSeriesManager = new BarSeriesManager(btcBarSeries);
        BarSeriesManager xrpSeriesManager = new BarSeriesManager(xrpBarSeries);
        TradingRecord btcShortRecord = btcSeriesManager.run(btcShort, TradeType.SELL);
        TradingRecord xrpShortRecord = xrpSeriesManager.run(xrpShort, TradeType.SELL);
        TradingRecord btcLongRecord = btcSeriesManager.run(btcLong, TradeType.BUY);
        TradingRecord xrpLongRecord = xrpSeriesManager.run(xrpLong, TradeType.BUY);

        LOGGER.info("Building Charts");
        TimeSeriesChart spreadChart = new TimeSeriesChart("BTCUSD - XRPUSD Spread");
        spreadChart.buildDataset("Pair Delta", btcBarSeries, pair);
        spreadChart.buildDataset("BBM", btcBarSeries, bbm);
        spreadChart.buildDataset("BBL", btcBarSeries, bbl);
        spreadChart.buildDataset("BBU", btcBarSeries, bbu);
        spreadChart.displayChart();

        TimeSeriesChart pairChart = new TimeSeriesChart("Pair Spread Chart");
        pairChart.buildDataset("BTCUSD", btcBarSeries, btcSigma);
        pairChart.buildDataset("XRPUSD", xrpBarSeries, xrpSigma);
        pairChart.addMarkers(btcBarSeries, btcShortRecord, btcShort);
        pairChart.addMarkers(xrpBarSeries, xrpShortRecord, xrpShort);
        pairChart.addMarkers(btcBarSeries, btcLongRecord, btcLong);
        pairChart.addMarkers(xrpBarSeries, xrpLongRecord, xrpLong);
        pairChart.displayChart();

        AnalysisCriterion maxDrawdown = new MaximumDrawdownCriterion();
        AnalysisCriterion grossReturn = new GrossReturnCriterion();
        AnalysisCriterion linearCost = new LinearTransactionCostCriterion(100, 0.00075);
        
        DecimalFormat formatter = new DecimalFormat("###.##");
        System.out.println("Number of BTC Trades: " + btcShortRecord.getPositionCount() + btcLongRecord.getPositionCount());
        System.out.println("Number of XRP Trades: " + xrpShortRecord.getPositionCount() + xrpLongRecord.getPositionCount());

        System.out.println("BTC Maximum Short Drawdown: " + formatter.format(maxDrawdown.calculate(btcBarSeries, btcShortRecord).doubleValue() * 100));
        System.out.println("BTC Maximum Long Drawdown: " + formatter.format(maxDrawdown.calculate(btcBarSeries, btcLongRecord).doubleValue() * 100));
        System.out.println("XRP Maximum Short Drawdown: " + formatter.format(maxDrawdown.calculate(xrpBarSeries, xrpShortRecord).doubleValue() * 100));
        System.out.println("XRP Maximum Long Drawdown: " + formatter.format(maxDrawdown.calculate(xrpBarSeries, xrpLongRecord).doubleValue() * 100));

        System.out.println("BTC Short Returns: " + formatter.format(grossReturn.calculate(btcBarSeries, btcShortRecord).doubleValue() * 100 - 100));
        System.out.println("XRP Short Returns: " + formatter.format(grossReturn.calculate(xrpBarSeries, xrpShortRecord).doubleValue() * 100 - 100));
        System.out.println("BTC Long Returns: " + formatter.format(grossReturn.calculate(btcBarSeries, btcLongRecord).doubleValue() * 100 - 100));
        System.out.println("XRP Long Returns: " + formatter.format(grossReturn.calculate(xrpBarSeries, xrpLongRecord).doubleValue() * 100 - 100));

        String tradingCost = formatter.format(linearCost.calculate(btcBarSeries, btcShortRecord).doubleValue() 
            + linearCost.calculate(xrpBarSeries, xrpShortRecord).doubleValue()
            + linearCost.calculate(btcBarSeries, btcLongRecord).doubleValue()
            + linearCost.calculate(xrpBarSeries, xrpLongRecord).doubleValue());

        System.out.println("Total trading Cost (%): " + tradingCost);
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
