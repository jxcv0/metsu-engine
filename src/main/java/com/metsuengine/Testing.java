package com.metsuengine;

import com.metsuengine.indicators.DifferenceIndicator;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Testing {
    public static void main(String[] args) {


        final TickSeries usd = new TickSeries("usd");
        final TickSeries usdt = new TickSeries("usdt");
        final DifferenceIndicator differenceIndicator = new DifferenceIndicator("Difference", usd, usdt);
        
        TimeSeriesChart chart = new TimeSeriesChart("Testing Chart", false);
        chart.addTickSeries(usd);
        chart.addTickSeries(usdt);
        chart.addIndicator(differenceIndicator);

        CSVManager usdManager = new CSVManager("BTCUSD2021-10-15.csv", usd);
        CSVManager usdtManager = new CSVManager("BTCUSDT2021-10-15.csv", usdt);
        usdManager.simulateTrades();
        usdtManager.simulateTrades();
    }
}
