package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MeanReversion {
    public static void main(String[] args) {

        final MovingAverage movingAverage = new MovingAverage(10000);
        final UpperStandardDeviationBand lowerBand = new UpperStandardDeviationBand(10000);
        final LowerStandardDeviationBand upperBand = new LowerStandardDeviationBand(10000);

        final TradeSeries tradeSeries = new TradeSeries(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                TradeSeries source = (TradeSeries) event.getSource();
                Trade trade = source.getLastTrade();
                movingAverage.addTrade(trade);
                lowerBand.addTrade(trade);
                upperBand.addTrade(trade);
            }
            
        });

        CSVManager csvManager = new CSVManager("BTCUSD2021-9-14.csv", tradeSeries);
        csvManager.createFromCSV();

        TimeSeriesChart chart = new TimeSeriesChart("BTCUSD2021-9-14");
        chart.buildDataset(movingAverage.getTimeSeries());
        chart.buildDataset(upperBand.getTimeSeries());
        chart.buildDataset(lowerBand.getTimeSeries());
        chart.buildDataset(tradeSeries);
        chart.displayChart();
    }
}
