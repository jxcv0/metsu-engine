package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MeanReversion {
    public static void main(String[] args) {
        
        final MovingAverage movingAverage = new MovingAverage(10000);
        final StandardDeviationBandsPair oneStdDev = new StandardDeviationBandsPair(10000, 1);
        final StandardDeviationBandsPair twoStdDev = new StandardDeviationBandsPair(10000, 2);

        final TradeSeries tradeSeries = new TradeSeries(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                TradeSeries source = (TradeSeries) event.getSource();
                Trade trade = source.getLastTrade();
                System.out.println(trade.time());

                movingAverage.addTradeToTimeSeries(trade);
                oneStdDev.addTradeToTimeSeries(trade);
                twoStdDev.addTradeToTimeSeries(trade);
            }
            
        });

        CSVManager csvManager = new CSVManager("BTCUSD2021-9-14.csv", tradeSeries);
        csvManager.createFromCSV();

        TimeSeriesChart chart = new TimeSeriesChart("BTCUSD2021-9-14");

        chart.buildDataset("Moving Average", movingAverage.getTimeSeries());
        chart.buildDataset("One Standard Deviation", oneStdDev.getUpperBandTimeSeries());
        chart.buildDataset("One Standard Deviation", oneStdDev.getLowerBandTimeSeries());
        chart.buildDataset("Two Standard Deviations", twoStdDev.getUpperBandTimeSeries());
        chart.buildDataset("Two Standard Deviations", twoStdDev.getLowerBandTimeSeries());
        chart.buildDataset("Trade Series", tradeSeries);
        chart.displayChart();
    }
}
