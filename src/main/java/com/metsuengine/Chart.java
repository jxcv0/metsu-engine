package com.metsuengine;

import java.awt.Dimension;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;

public class Chart {

    private TimeSeries timeSeries;

    public Chart() {

    }

    private TimeSeries buildTimeSeries(String name, BarSeries barSeries) {

        this.timeSeries = new TimeSeries(name); 

        for (Bar bar : barSeries.getBarData()) {
            timeSeries.add(new Second(Date.from(bar.getEndTime().toInstant())), bar.getClosePrice().doubleValue());
        }

        return timeSeries;
    }

    private void displayChart(JFreeChart chart) {

        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setPreferredSize(new Dimension(1000, 600));

        ApplicationFrame frame = new ApplicationFrame("Chart");
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public void buildTimeSeriesChart(String name, BarSeries barSeries) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(buildTimeSeries(name, barSeries));

        JFreeChart chart = ChartFactory.createTimeSeriesChart(name, "Time", "Price", dataset);
        displayChart(chart);
    }
}
