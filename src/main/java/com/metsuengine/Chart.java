package com.metsuengine;

import java.awt.Dimension;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Chart extends ApplicationFrame {

    private JFreeChart chart;

    public Chart(String applicationTitle, String title, VolumeProfile volumeProfile) {
        super(applicationTitle);
        this.chart = ChartFactory.createXYBarChart(title, "Volume", false, "Price", createDataset(volumeProfile));

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(panel);
    }

    private IntervalXYDataset createDataset(VolumeProfile volumeProfile) {

        final XYSeries dataset = new XYSeries("");

        for (Double level : volumeProfile.getHashMap().keySet()) {
            dataset.add(level, volumeProfile.getHashMap().get(level));
        }

        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        return xySeriesCollection;
    }

    public void displayChart() {

        ChartPanel panel = new ChartPanel(this.chart);
        panel.setFillZoomRectangle(true);
        panel.setPreferredSize(new Dimension(1000, 600));

        ApplicationFrame frame = new ApplicationFrame("Chart");
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
