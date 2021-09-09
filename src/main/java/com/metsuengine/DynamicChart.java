package com.metsuengine;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class DynamicChart extends ApplicationFrame {

    private TimeSeries series;

    public DynamicChart(String title) {
        super(title);
        this.series = new TimeSeries("Data");
        TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
        JFreeChart chart = createChart(dataset);

        ChartPanel chartPanel = new ChartPanel(chart);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(chartPanel);
        chartPanel.setPreferredSize(new Dimension(500, 270));
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "BTCUSD-Bybit",
            "Time",
            "price",
            dataset,
            true,
            true,
            false 
        );

        XYPlot plot = chart.getXYPlot();

        ValueAxis valueAxis = plot.getDomainAxis();
        valueAxis.setAutoRange(true);
        valueAxis.setFixedAutoRange(120000.0); // get from frameseries?

        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setAutoRange(true);

        return chart;
    }

}
