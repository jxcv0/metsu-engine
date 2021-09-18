package com.metsuengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Chart extends ApplicationFrame {

    private JFreeChart chart;

    public Chart(String applicationTitle, String title, VolumeDistribution volumeDistribution) {
        super(applicationTitle);
        this.chart = ChartFactory.createXYBarChart(title, "Price", false, "Volume", createDataset(volumeDistribution));

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(panel);
    }

    private IntervalXYDataset createDataset(VolumeDistribution volumeDistribution) {

        final XYSeries series = new XYSeries("");

        for (Double level : volumeDistribution.getHashMap().keySet()) {
            series.add(level, volumeDistribution.getHashMap().get(level));
        }

        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeriesCollection.addSeries(series);
        return xySeriesCollection;
    }

    public void displayChart() {

        ChartPanel panel = new ChartPanel(this.chart);
        panel.setFillZoomRectangle(true);
        panel.setPreferredSize(new Dimension(1200, 800));

        ApplicationFrame frame = new ApplicationFrame("Chart");
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);

        XYBarRenderer renderer = new XYBarRenderer(0.575);
        renderer.setBarPainter(new StandardXYBarPainter() {
                @Override
                public void paintBarShadow(Graphics2D g2, XYBarRenderer renderer, int row,
                    int column, RectangularShape bar, RectangleEdge base,
                    boolean pegShadow){}
        });
        renderer.setSeriesPaint(0, new Color(50, 75, 100));

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        plot.setRenderer(renderer);
    }
}
