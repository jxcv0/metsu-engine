package com.metsuengine;

import java.awt.Dimension;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartColor;
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
    
    private static TimeSeries buildTimeSeries(String name, FrameSeries frameSeries, List<Double> list) {

        TimeSeries timeSeries = new TimeSeries(name); 

        for (int i = 0; i < frameSeries.getFrameCount(); i++) {
            Frame frame = frameSeries.getFrame(i);
            timeSeries.addOrUpdate(new Second(Date.from(frame.getTime().toInstant())), list.get(i));
        }

        return timeSeries;
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

    public static void buildTimeSeriesChart(FrameSeries frameSeries) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(buildTimeSeries("Bid", frameSeries, frameSeries.getSeriesBestBid()));
        dataset.addSeries(buildTimeSeries("Ask", frameSeries, frameSeries.getSeriesBestAsk()));

        JFreeChart chart = ChartFactory.createTimeSeriesChart("BTCUSD", "Time", "Price", dataset);
        displayChart(chart);
    }

    public static void buildRatioChart(FrameSeries frameSeries) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(buildTimeSeries("Ratio", frameSeries, frameSeries.getSeriesOrderBookRatio()));

        JFreeChart chart = ChartFactory.createTimeSeriesChart("BTCUSD OrderBook Ratio", "Time", "Ratio", dataset);
        displayChart(chart);
    }

    public static void buildDifferenceChart(FrameSeries frameSeries) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(buildTimeSeries("Bid", frameSeries, frameSeries.getSeriesOrderBookDepth("Bid")));
        dataset.addSeries(buildTimeSeries("Ask", frameSeries, frameSeries.getSeriesOrderBookDepth("Ask")));

        JFreeChart chart = ChartFactory.createTimeSeriesChart("OrderBook Depth", "Time", "USD", dataset);

        XYDifferenceRenderer differenceRenderer = new XYDifferenceRenderer();
        differenceRenderer.setSeriesPaint(0, new ChartColor(50, 50, 50));
        differenceRenderer.setSeriesPaint(1, new ChartColor(50, 50, 50));

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setRenderer(differenceRenderer);

        displayChart(chart);

    }
}
