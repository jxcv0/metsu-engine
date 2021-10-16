package com.metsuengine;

import com.metsuengine.indicators.DifferenceIndicator;

public class Testing {
    public static void main(String[] args) {


        final TickSeries usd = new TickSeries("usd");
        final TickSeries usdt = new TickSeries("usdt");
        final DifferenceIndicator differenceIndicator = new DifferenceIndicator("Difference", usd, usdt);
        
        TimeSeriesChart chart = new TimeSeriesChart("Testing Chart", true);
        chart.addTickSeries(usd);
        chart.addTickSeries(usdt);
        chart.addIndicator(differenceIndicator);
        chart.displayChart();

        CSVManager usdManager = new CSVManager("BTCUSD2021-10-15.csv", usd);
        CSVManager usdtManager = new CSVManager("BTCUSDT2021-10-15.csv", usdt);
        Thread usdThread = new Thread(usdManager);
        Thread usdtThread = new Thread(usdtManager);
        usdThread.run();
        System.out.println("here");
        usdtThread.run();
    }
}
