package com.metsuengine;

import java.util.logging.Logger;

import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.indicators.volume.VWAPIndicator;

public class SimpleBot {

    private static final Logger LOGGER = Logger.getLogger(SimpleBot.class.getName());

    public static void main(String[] args) {
        // int to = 1633175561;
        // int from = to - 86400;
        // createKlineCSV(from, to);

        LOGGER.info("Getting kline data from CSV");
        CSVManager manager = new CSVManager("BTCUSD-inSampleKline.csv");
        BarSeries barSeries = manager.barSeriesFromCSV();

        Strategy vwapStdDevStrategy = Strategies.bollingerBandsStrategyLong(barSeries);

        BarSeriesManager barSeriesManager = new BarSeriesManager(barSeries);
        TradingRecord tradingRecord = barSeriesManager.run(vwapStdDevStrategy);
        
        LOGGER.info("Creating chart indicators");
        VWAPIndicator vwap = new VWAPIndicator(barSeries, 100);
        StandardDeviationIndicator stdDev = new StandardDeviationIndicator(vwap, 100);
        BollingerBandsMiddleIndicator middleIndicator = new BollingerBandsMiddleIndicator(vwap);
        BollingerBandsLowerIndicator lowerIndicator = new BollingerBandsLowerIndicator(middleIndicator, stdDev);
        BollingerBandsUpperIndicator upperIndicator = new BollingerBandsUpperIndicator(middleIndicator, stdDev);
        
        LOGGER.info("Building Chart");
        TimeSeriesChart chart = new TimeSeriesChart("BTCUSD");
        chart.buildDataset("Close Price", barSeries);
        chart.buildDataset("40 SMA", barSeries, middleIndicator);
        chart.buildDataset("40 SMA", barSeries, lowerIndicator);
        chart.buildDataset("40 SMA", barSeries, upperIndicator);
        chart.displayChart();
    }

    public static void createKlineCSV(int from, int to) {
        BybitEndpoint endpoint = new BybitEndpoint("BTCUSD");

        LOGGER.info("Getting kline data");
        for (int i = from; i < to; i+=60) {
            endpoint.getKlineRecords(i);
        }
    }
}
