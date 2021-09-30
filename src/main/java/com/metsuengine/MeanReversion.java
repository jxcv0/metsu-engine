package com.metsuengine;

import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MeanReversion {

    private static final Logger LOGGER = Logger.getLogger(MeanReversion.class.getName());
    public static void main(String[] args) {
        
        LOGGER.info("Creating Indicators");

        final MovingAverage movingAverage = new MovingAverage(10000);
        final StandardDeviationBandsPair oneStdDev = new StandardDeviationBandsPair(10000, 1);
        final StandardDeviationBandsPair twoStdDev = new StandardDeviationBandsPair(10000, 2);

        BackTest backTest = new BackTest(movingAverage);

        LOGGER.info("Creating TradeSeries");

        final TradeSeries tradeSeries = new TradeSeries(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                TradeSeries source = (TradeSeries) event.getSource();
                Trade trade = source.getLastTrade();

                movingAverage.addTradeToTimeSeries(trade);
                oneStdDev.addTradeToTimeSeries(trade);
                twoStdDev.addTradeToTimeSeries(trade);
                backTest.checkCrossedDown(trade);
            }
            
        });
        
        LOGGER.info("Loading trades from CSV");
        CSVManager csvManager = new CSVManager("BTCUSD2021-9-14.csv", tradeSeries);
        csvManager.createFromCSV();

        TimeSeriesChart chart = new TimeSeriesChart("BTCUSD2021-9-14");

        LOGGER.info("Building Datasets");

        chart.buildDataset("Moving Average", movingAverage.getTimeSeries());
        chart.buildDataset("One Standard Deviation", oneStdDev.getUpperBandTimeSeries());
        chart.buildDataset("One Standard Deviation", oneStdDev.getLowerBandTimeSeries());
        chart.buildDataset("Two Standard Deviations", twoStdDev.getUpperBandTimeSeries());
        chart.buildDataset("Two Standard Deviations", twoStdDev.getLowerBandTimeSeries());
        chart.buildDataset("Trade Series", tradeSeries);
        chart.setMarkers(backTest.crossUp());

        LOGGER.info("Displaying Chart");

        chart.displayChart();
    }
}
