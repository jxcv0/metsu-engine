package com.metsuengine;

import java.awt.Dimension;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class TimeSeriesChart extends ApplicationFrame {

    private final JFreeChart chart;
    private final TimeSeriesCollection dataset;

    public TimeSeriesChart(String title) {
        super(title);
        this.dataset = new TimeSeriesCollection();
        this.chart = ChartFactory.createTimeSeriesChart(title, "Time", "Price", dataset);
        chart.removeLegend();

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(panel);
    }

    public void buildDataset(String title, TradeSeries tradeSeries) {
        TimeSeries timeSeries = new TimeSeries(title);
        for (Trade trade : tradeSeries) {
            timeSeries.addOrUpdate(new Millisecond(Date.from(trade.time().toInstant())), trade.price());
        }
        dataset.addSeries(timeSeries);
    }

    public void buildDataset(String title, HashMap<ZonedDateTime, Double> indicator) {
        TimeSeries timeSeries = new TimeSeries(title);
        for (ZonedDateTime time : indicator.keySet()) {
            timeSeries.addOrUpdate(new Millisecond(Date.from(time.toInstant())), indicator.get(time));
        }
        dataset.addSeries(timeSeries);
    }

    public void displayChart() {

        ChartPanel panel = new ChartPanel(this.chart);
        panel.setFillZoomRectangle(true);
        panel.setPreferredSize(new Dimension(1200, 800));
        panel.setMouseWheelEnabled(true);

        ApplicationFrame frame = new ApplicationFrame(this.getTitle());
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);

    }
}
