package com.metsuengine;

import java.time.ZonedDateTime;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Backtest {
    public static void main(String[] args) {

        System.out.println(ZonedDateTime.now());

        VolumeDistribution volumeDistribution = new VolumeDistribution();

        TradeSeries tradeSeries = new TradeSeries(new ChangeListener() {

            int count = 0;

            @Override
            public void stateChanged(ChangeEvent event) {
                TradeSeries source = (TradeSeries) event.getSource();
                volumeDistribution.update(source.getLastTrade());
                volumeDistribution.addMissingValues();
                volumeDistribution.normalize();
                volumeDistribution.filter();
                count++;
                System.out.println(count);
            }            
        });
        
        CSVManager manager = new CSVManager("BTCUSD2021-09-14.csv", tradeSeries);
        tradeSeries = manager.createFromCSV();
        
        Chart volumeProfileChart = new Chart("Volume Distribution Chart", "Volume Distribution", volumeDistribution);
        Chart filteredProfile = new Chart("Filtered Volume Distribution", "Filtered Volume Distribution", volumeDistribution);
        volumeProfileChart.displayChart();
        filteredProfile.displayChart();

        System.out.println(ZonedDateTime.now());

    }
}