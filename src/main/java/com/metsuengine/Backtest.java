package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Backtest {
    public static void main(String[] args) {

        VolumeDistribution volumeDistribution = new VolumeDistribution();

        TradeSeries tradeSeries = new TradeSeries(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                TradeSeries source = (TradeSeries) event.getSource();
                volumeDistribution.update(source.getLastTrade());
            }            
        });
        
        CSVManager manager = new CSVManager("BTCUSD2021-09-13.csv", tradeSeries);
        tradeSeries = manager.createFromCSV();
        
        volumeDistribution.addMissingValues();
        volumeDistribution.normalize();
        // volumeDistribution.filter();

        Chart volumeProfileChart = new Chart("Volume Distribution Chart", "Volume Distribution", volumeDistribution);
        volumeProfileChart.displayChart();
    }
}