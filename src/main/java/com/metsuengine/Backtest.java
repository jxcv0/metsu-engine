package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Backtest {

    public static void main(String[] args) {
        
        // Create distribution of previous day 
        TradeSeries previousDayTrades = new TradeSeries();

        CSVManager oldManager = new CSVManager("BTCUSD2021-09-13.csv", previousDayTrades);
        previousDayTrades = oldManager.createFromCSV();
        
        VolumeDistribution previousDayDistribution = new VolumeDistribution(previousDayTrades, true);
        System.out.println("prev: " + previousDayTrades.vwap());
        System.out.println("prev: " + previousDayDistribution.vwap());
        
        Chart volumeProfileChart = new Chart("13-09 Volume Distribution Chart", "Volume Distribution", previousDayDistribution);
        volumeProfileChart.displayChart();

        // creating next day series
        TradeSeries currentTrades = new TradeSeries(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                TradeSeries source = (TradeSeries) e.getSource();
                
            }          
        });

        CSVManager currentManager = new CSVManager("BTCUSD2021-09-14.csv", currentTrades);
        currentTrades = currentManager.createFromCSV();

        System.out.println("curr: " + currentTrades.vwap());
        System.out.println("curr: " + currentTrades.vwap());

    }
}