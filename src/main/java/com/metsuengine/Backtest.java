package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Backtest {

    public static void main(String[] args) {
        
        // Create distribution of previous day 
        TradeSeries previousDayTrades = new TradeSeries();

        CSVManager oldManager = new CSVManager("BTCUSD2021-09-13.csv", previousDayTrades);
        previousDayTrades = oldManager.createFromCSV();
        
        VolumeDistribution previousDayDistribution = new VolumeDistribution(previousDayTrades);
        
        Chart volumeProfileChart = new Chart("13-09 Volume Distribution Chart", "Volume Distribution", previousDayDistribution.smooth().normalize());
        volumeProfileChart.displayChart();

        // Instansiate strategy
        Strategy strategy = new Strategy(
                previousDayDistribution.highVolumeNodes(), 
                previousDayDistribution.lowVolumeNodes());

        // creating next day series
        final TradeSeries currentTrades = new TradeSeries();

        currentTrades.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                TradeSeries source = (TradeSeries) e.getSource();
                strategy.update(source.getLastTrade(), currentTrades.vwap());
            }          
        });

        CSVManager currentManager = new CSVManager("BTCUSD2021-09-14.csv", currentTrades);
        currentTrades.setSeries(currentManager.createFromCSV().getTrades());

    }
}