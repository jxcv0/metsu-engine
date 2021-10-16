package com.metsuengine;

import com.metsuengine.indicators.DifferenceIndicator;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Testing {
    public static void main(String[] args) {

        TimeSeriesChart chart = new TimeSeriesChart("Testing Chart");

        final TickSeries usd = new TickSeries("usd");
        final TickSeries usdt = new TickSeries("usdt");
        final DifferenceIndicator differenceIndicator = new DifferenceIndicator("Difference", usd, usdt);
        CSVManager usdManager = new CSVManager("BTCUSD2021-10-15.csv", usd);
        CSVManager usdtManager = new CSVManager("BTCUSDT2021-10-15.csv", usdt);
        usdManager.simulateTrades();
        usdtManager.simulateTrades();
        chart.displayChart();
        
        DescriptiveStatistics descStat = new DescriptiveStatistics(differenceIndicator.toArray());
        System.out.println(descStat.getMean());
    }
}
