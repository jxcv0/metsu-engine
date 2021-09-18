package com.metsuengine;

public class Backtester {
    public static void main(String[] args) {
        
        CSVManager manager = new CSVManager("BTCUSD2021-09-14.csv");
        TradeSeries tradeSeries = manager.createFromCSV();
        VolumeDistribution volumeDistribution = new VolumeDistribution(tradeSeries);

        Chart chart = new Chart("Chart", "Volume Distribution", volumeDistribution);
        chart.displayChart();

        // for backtesting later
        // tradeSeries.addChangeListener(new ChangeListener(){

        //     @Override
        //     public void stateChanged(ChangeEvent e) {
        //         eeeeee
        //     }            
        // });
    }
}
