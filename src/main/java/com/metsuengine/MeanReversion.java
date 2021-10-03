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

        final TickSeries tickSeries = new TickSeries(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                TickSeries source = (TickSeries) event.getSource();
                Tick tick = source.getLastTick();

                movingAverage.addTickToTimeSeries(tick);
                stdDev.addTickToTimeSeries(tick);
            }
            
        });
        
        LOGGER.info("Loading trades from CSV");
        CSVManager csvManager = new CSVManager("BTCUSD2021-9-14.csv", tickSeries);
        csvManager.createFromCSV();

        TimeSeriesChart chart = new TimeSeriesChart("BTCUSD2021-9-14");

        LOGGER.info("Building Datasets");

        chart.buildDataset("Moving Average", movingAverage.getTimeSeries());
        chart.buildDataset("One Standard Deviation", stdDev.getUpperBandTimeSeries());
        chart.buildDataset("One Standard Deviation", stdDev.getLowerBandTimeSeries());
        chart.buildDataset("Trade Series", tickSeries);

        LOGGER.info("Displaying Chart");

        chart.displayChart();
    }
}
