package com.metsuengine;

import java.awt.Dimension;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class Chart {
    
    private static TimeSeries buildTimeSeries(String name, BarSeries barSeries) {

        TimeSeries timeSeries = new TimeSeries(name); 

        for (int i = 0; i < barSeries.getBarCount(); i++) {
            Bar bar = barSeries.getBar(i);
            timeSeries.addOrUpdate(new Second(Date.from(bar.getTime().toInstant())), bar.getPrice());
        }

        return timeSeries;
    }

    private static void displayChart(JFreeChart chart) {

        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setPreferredSize(new Dimension(1000, 540));

        ApplicationFrame frame = new ApplicationFrame("Chart");
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void buildChart(BarSeries barSeries) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(buildTimeSeries("Close", barSeries));
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart("BTCUSD", "Time", "Price", dataset);

        // XYPlot plot = (XYPlot) chart.getPlot();

        displayChart(chart);
    }

    public static JFreeChart buildChart() {
        return new JFreeChart(new XYPlot());
    }

}
