package com.metsuengine;

import java.awt.Dimension;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class Chart {

    // TODO timeSeries builders could be a single method that takes a List<>?
    // requires new method that takes lists from Barseries or Sapshot
    // makes livecharts possible using Snapshot class
    
    private static TimeSeries buildTimeSeries(String name, BarSeries barSeries) {

        TimeSeries timeSeries = new TimeSeries(name); 

        for (int i = 0; i < barSeries.getBarCount(); i++) {
            Bar bar = barSeries.getBar(i);
            timeSeries.addOrUpdate(new Second(Date.from(bar.getTime().toInstant())), bar.getPrice());
        }

        return timeSeries;
    }

    private static TimeSeries buildRatioTimeSeries(String name, BarSeries barSeries) {

        TimeSeries timeSeries = new TimeSeries(name); 

        for (int i = 0; i < barSeries.getBarCount(); i++) {
            Bar bar = barSeries.getBar(i);
            timeSeries.addOrUpdate(new Second(Date.from(bar.getTime().toInstant())), bar.getDeltaRatio());
        }

        return timeSeries;
    }

    private static TimeSeries buildDifferenceTimeSeries(String name, BarSeries barSeries, String side) {

        TimeSeries timeSeries = new TimeSeries(name);
        
        if (side.equals("Bid")) {
            for (int i = 0; i < barSeries.getBarCount(); i++) {
                Bar bar = barSeries.getBar(i);
                timeSeries.addOrUpdate(new Second(Date.from(bar.getTime().toInstant())), bar.getBidDepth());
            }

            return timeSeries;
            
        } else if (side.equals("Ask")) {
            for (int i = 0; i < barSeries.getBarCount(); i++) {
                Bar bar = barSeries.getBar(i);
                timeSeries.addOrUpdate(new Second(Date.from(bar.getTime().toInstant())), bar.getAskDepth());
            }

            return timeSeries;
        } else {
            return null;
        }
    }

    private static void displayChart(JFreeChart chart) {

        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setPreferredSize(new Dimension(1000, 600));

        ApplicationFrame frame = new ApplicationFrame("Chart");
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void buildTimeSeriesChart(BarSeries barSeries) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(buildTimeSeries("Price", barSeries));
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart("BTCUSD", "Time", "Price", dataset);
        displayChart(chart);
    }

    public static void buildRatioChart(BarSeries barSeries) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(buildRatioTimeSeries("Ratio", barSeries));

        JFreeChart chart = ChartFactory.createTimeSeriesChart("BTCUSD OrderBook Ratio", "Time", "Ratio", dataset);
        displayChart(chart);
    }

    public static void buildDifferenceChart(BarSeries barSeries) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(buildDifferenceTimeSeries("Bid", barSeries, "Bid"));
        dataset.addSeries(buildDifferenceTimeSeries("Ask", barSeries, "Ask"));

        JFreeChart chart = ChartFactory.createTimeSeriesChart("OrderBook Depth", "Time", "USD", dataset);

        XYDifferenceRenderer differenceRenderer = new XYDifferenceRenderer();
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setRenderer(differenceRenderer);

        displayChart(chart);

    }
}
