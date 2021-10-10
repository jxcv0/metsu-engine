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
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.DifferenceIndicator;
import org.ta4j.core.indicators.helpers.FixedIndicator;
import org.ta4j.core.indicators.statistics.SigmaIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

public class Backtest {

    private static final Logger LOGGER = Logger.getLogger(Backtest.class.getName());

    public static void main(String[] args) {

        // createPairsKlineCSV(ZonedDateTime.now(ZoneOffset.UTC).minusMonths(1), ZonedDateTime.now(ZoneOffset.UTC));

        LOGGER.info("Getting kline data from CSV");
        CSVManager btcManager = new CSVManager("BTCUSD10-10.csv");
        CSVManager altManager = new CSVManager("ETHUSD10-10.csv");
        BarSeries btcBarSeries = btcManager.barSeriesFromCSV();
        BarSeries altBarSeries = altManager.barSeriesFromCSV();
        
        FixedIndicator<Num> zero = new FixedIndicator<Num>(btcBarSeries, DecimalNum.valueOf(0)); 
        for (int i = 0; i < btcBarSeries.getBarCount(); i++) {
            zero.addValue(DecimalNum.valueOf(0));
        }

        ClosePriceIndicator btcClose = new ClosePriceIndicator(btcBarSeries);
        ClosePriceIndicator altClose = new ClosePriceIndicator(altBarSeries);
        SigmaIndicator btcSigma = new SigmaIndicator(btcClose, 10000);
        SigmaIndicator altSigma = new SigmaIndicator(altClose, 10000);
        DifferenceIndicator pair = new DifferenceIndicator(btcSigma, altSigma);
        StandardDeviationIndicator deviation = new StandardDeviationIndicator(pair, 10000);
        BollingerBandsMiddleIndicator bbm = new BollingerBandsMiddleIndicator(zero);
        BollingerBandsLowerIndicator bbl = new BollingerBandsLowerIndicator(bbm, deviation);
        BollingerBandsUpperIndicator bbu = new BollingerBandsUpperIndicator(bbm, deviation);
        BollingerBandsLowerIndicator tpl = new BollingerBandsLowerIndicator(bbm, deviation, DecimalNum.valueOf(0.25));
        BollingerBandsUpperIndicator tpu = new BollingerBandsUpperIndicator(bbm, deviation, DecimalNum.valueOf(0.25));

        // Strategy btcLong = new BaseStrategy("BTC", new CrossedDownIndicatorRule(pair, bbl), new CrossedUpIndicatorRule(pair, tpl));
        // Strategy altShort = new BaseStrategy("ALT",  new CrossedDownIndicatorRule(pair, bbl), new CrossedUpIndicatorRule(pair, tpl));
        Strategy btcShort = new BaseStrategy("BTC",  new CrossedUpIndicatorRule(pair, bbu), new CrossedDownIndicatorRule(pair, tpu));
        Strategy altLong = new BaseStrategy("ALT",  new CrossedUpIndicatorRule(pair, bbu), new CrossedDownIndicatorRule(pair, tpu));
        

        BarSeriesManager btcSeriesManager = new BarSeriesManager(btcBarSeries);
        BarSeriesManager altSeriesManager = new BarSeriesManager(altBarSeries);
        TradingRecord btcShortRecord = btcSeriesManager.run(btcShort, TradeType.SELL);
        // TradingRecord altShortRecord = altSeriesManager.run(altShort, TradeType.SELL);
        // TradingRecord btcLongRecord = btcSeriesManager.run(btcLong, TradeType.BUY);
        TradingRecord altLongRecord = altSeriesManager.run(altLong, TradeType.BUY);

        LOGGER.info("Building Charts");
        TimeSeriesChart spreadChart = new TimeSeriesChart("BTCUSD - ALTUSD Spread");
        spreadChart.buildDataset("Pair Delta", btcBarSeries, pair);
        spreadChart.buildDataset("BBM", btcBarSeries, bbm);
        spreadChart.buildDataset("BBL", btcBarSeries, bbl);
        spreadChart.buildDataset("BBU", btcBarSeries, bbu);
        spreadChart.buildDataset("TPL", btcBarSeries, tpl);
        spreadChart.buildDataset("TPU", btcBarSeries, tpu);
        // spreadChart.addMarkers(altBarSeries, altShortRecord, altShort);
        spreadChart.addMarkers(altBarSeries, altLongRecord, altLong);
        spreadChart.displayChart();

        TimeSeriesChart pairChart = new TimeSeriesChart("Pair Spread Chart");
        pairChart.buildDataset("BTCUSD", btcBarSeries, btcSigma);
        pairChart.buildDataset("ALTUSD", altBarSeries, altSigma);
        // pairChart.addMarkers(altBarSeries, altShortRecord, altShort);
        pairChart.addMarkers(altBarSeries, altLongRecord, altLong);
        pairChart.displayChart();

        AnalysisCriterion maxDrawdown = new MaximumDrawdownCriterion();
        AnalysisCriterion grossReturn = new GrossReturnCriterion();
        AnalysisCriterion linearCost = new LinearTransactionCostCriterion(100, 0.00075);
        
        DecimalFormat formatter = new DecimalFormat("###.##");
        System.out.println("Number of BTC Trades: " + btcShortRecord.getPositionCount()); // + btcLongRecord.getPositionCount());
        System.out.println("Number of ALT Trades: " + altLongRecord.getPositionCount()); // + altShortRecord.getPositionCount());
        
        // System.out.println("BTC Maximum Long Drawdown: " + formatter.format(maxDrawdown.calculate(btcBarSeries, btcLongRecord).doubleValue() * 100));
        System.out.println("BTC Maximum Short Drawdown: " + formatter.format(maxDrawdown.calculate(btcBarSeries, btcShortRecord).doubleValue() * 100));
        System.out.println("ALT Maximum Long Drawdown: " + formatter.format(maxDrawdown.calculate(altBarSeries, altLongRecord).doubleValue() * 100));
        // System.out.println("ALT Maximum Short Drawdown: " + formatter.format(maxDrawdown.calculate(altBarSeries, altShortRecord).doubleValue() * 100));

        // System.out.println("BTC Long Returns: " + formatter.format(grossReturn.calculate(btcBarSeries, btcLongRecord).doubleValue() * 100 - 100));
        System.out.println("BTC Short Returns: " + formatter.format(grossReturn.calculate(btcBarSeries, btcShortRecord).doubleValue() * 100 - 100));
        System.out.println("ALT Long Returns: " + formatter.format(grossReturn.calculate(altBarSeries, altLongRecord).doubleValue() * 100 - 100));
        // System.out.println("ALT Short Returns: " + formatter.format(grossReturn.calculate(altBarSeries, altShortRecord).doubleValue() * 100 - 100));

        double tradingCost = Double.parseDouble(formatter.format(linearCost.calculate(btcBarSeries, btcShortRecord).doubleValue() 
            // + linearCost.calculate(altBarSeries, altShortRecord).doubleValue()
            // + linearCost.calculate(btcBarSeries, btcLongRecord).doubleValue()
            + linearCost.calculate(altBarSeries, altLongRecord).doubleValue()));
        
        double totalReturn = (// grossReturn.calculate(btcBarSeries, btcLongRecord).doubleValue() * 100 - 100)
            + (grossReturn.calculate(btcBarSeries, btcShortRecord).doubleValue() * 100 - 100)
            + (grossReturn.calculate(altBarSeries, altLongRecord).doubleValue() * 100 - 100));
            // + (grossReturn.calculate(altBarSeries, altShortRecord).doubleValue() * 100 - 100);

        System.out.println("Total trading Cost (%): " + tradingCost);
        System.out.println("Total returns (inc. cost): " + (totalReturn - tradingCost));
    }

    public static void createKlineCSV(int from, int to) {
        BybitEndpoint endpoint = new BybitEndpoint("BTCUSD");

        LOGGER.info("Getting kline data");
        for (int i = from; i < to; i+=60) {
            endpoint.getKlineRecord(i);
        }
    }

    public static void createPairsKlineCSV(ZonedDateTime from, ZonedDateTime to) {
        BybitEndpoint firstEndpoint = new BybitEndpoint("BTCUSD");
        BybitEndpoint secondEndpoint = new BybitEndpoint("ETHUSD");
        LOGGER.info("Getting kline data");
        while (from.isBefore(to)) {
            firstEndpoint.get200KlineRecords((int) from.toEpochSecond(), "1");
            secondEndpoint.get200KlineRecords((int) from.toEpochSecond(), "1");
            from = from.plusMinutes(200);
        }
    }
}
