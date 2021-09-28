package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// TODO needs to be far more robust
// redo :()
public class Backtest {
    public static void main(String[] args) {

        double runningCurve = 1;

        runningCurve *= run("BTCUSD2021-8-22.csv", "BTCUSD2021-8-23.csv");
        runningCurve *= run("BTCUSD2021-8-23.csv", "BTCUSD2021-8-24.csv");
        runningCurve *= run("BTCUSD2021-8-24.csv", "BTCUSD2021-8-25.csv");
        runningCurve *= run("BTCUSD2021-8-25.csv", "BTCUSD2021-8-26.csv");
        runningCurve *= run("BTCUSD2021-8-26.csv", "BTCUSD2021-8-27.csv");
        runningCurve *= run("BTCUSD2021-8-27.csv", "BTCUSD2021-8-28.csv");
        runningCurve *= run("BTCUSD2021-8-28.csv", "BTCUSD2021-8-29.csv");
        runningCurve *= run("BTCUSD2021-8-29.csv", "BTCUSD2021-8-30.csv");
        runningCurve *= run("BTCUSD2021-8-30.csv", "BTCUSD2021-8-31.csv");
        runningCurve *= run("BTCUSD2021-8-31.csv", "BTCUSD2021-9-1.csv");
        runningCurve *= run("BTCUSD2021-9-1.csv", "BTCUSD2021-9-2.csv");
        
        runningCurve *= run("BTCUSD2021-9-4.csv", "BTCUSD2021-9-5.csv");
        runningCurve *= run("BTCUSD2021-9-5.csv", "BTCUSD2021-9-6.csv");
        runningCurve *= run("BTCUSD2021-9-6.csv", "BTCUSD2021-9-7.csv");
        runningCurve *= run("BTCUSD2021-9-7.csv", "BTCUSD2021-9-8.csv");
        runningCurve *= run("BTCUSD2021-9-8.csv", "BTCUSD2021-9-9.csv");
        runningCurve *= run("BTCUSD2021-9-9.csv", "BTCUSD2021-9-10.csv");
        runningCurve *= run("BTCUSD2021-9-10.csv", "BTCUSD2021-9-11.csv");
        runningCurve *= run("BTCUSD2021-9-11.csv", "BTCUSD2021-9-12.csv");
        runningCurve *= run("BTCUSD2021-9-12.csv", "BTCUSD2021-9-13.csv");
        runningCurve *= run("BTCUSD2021-9-13.csv", "BTCUSD2021-9-14.csv");
        
        System.out.println(runningCurve);
    }

    public static double run(String previousDay, String currentDay) {
        
        final EquityModel equityModel = new EquityModel(1.0);
               
        final TradeSeries previousDayTrades = new TradeSeries();

        CSVManager oldManager = new CSVManager(previousDay, previousDayTrades);
        oldManager.createFromCSV();
        
        VolumeDistribution previousDayDistribution = new VolumeDistribution(previousDayTrades);
        
        final TradeSeries currentTrades = new TradeSeries();

        Strategy strategy = new Strategy(
                previousDayDistribution.highVolumeNodes(), 
                previousDayDistribution.lowVolumeNodes(),
                equityModel);

        currentTrades.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                TradeSeries source = (TradeSeries) e.getSource();
                strategy.update(source.getLastTrade());
            }          
        });

        CSVManager currentManager = new CSVManager(currentDay, currentTrades);
        currentManager.createFromCSV();
        strategy.updateEquity();

        return equityModel.equity();
    }
}