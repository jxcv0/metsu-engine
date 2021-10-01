package com.metsuengine;

import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MeanReversion {

    private static final Logger LOGGER = Logger.getLogger(MeanReversion.class.getName());
    public static void main(String[] args) {
        
        LOGGER.info("Creating Indicators");

        final MovingAverage movingAverage = new MovingAverage(10000);
        final StandardDeviationBandsPair stdDev = new StandardDeviationBandsPair(10000, 2);

        LOGGER.info("Creating TradeSeries");

        final TradeSeries tradeSeries = new TradeSeries(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                TradeSeries source = (TradeSeries) event.getSource();
                Trade trade = source.getLastTrade();

                movingAverage.addTradeToTimeSeries(trade);
                stdDev.addTradeToTimeSeries(trade);
            }
            
        });
        
        LOGGER.info("Loading trades from CSV");
        CSVManager csvManager = new CSVManager("BTCUSD2021-9-14.csv", tradeSeries);
        csvManager.createFromCSV();

        TimeSeriesChart chart = new TimeSeriesChart("BTCUSD2021-9-14");

        LOGGER.info("Building Datasets");

        chart.buildDataset("Moving Average", movingAverage.getTimeSeries());
        chart.buildDataset("One Standard Deviation", stdDev.getUpperBandTimeSeries());
        chart.buildDataset("One Standard Deviation", stdDev.getLowerBandTimeSeries());
        chart.buildDataset("Trade Series", tradeSeries);

        LOGGER.info("Displaying Chart");

        chart.displayChart();
    }
}
