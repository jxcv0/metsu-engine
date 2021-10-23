package com.metsuengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.RectangularShape;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Chart extends JFrame implements ChangeListener {
    
    private final XYSeriesCollection tickSeriesDataset;
    private final XYSeriesCollection orderbookDataset;
    private TickDistribution distribution;
 
    /**
     * Constructor
     * 
     * @param title the title of the timeseries chart
     */
    public Chart(String title) {
        super(title);
        this.tickSeriesDataset = new XYSeriesCollection();
        this.orderbookDataset = new XYSeriesCollection();
    }
    
    /**
     * plots a Time-Price chart from a TickSeries
     * 
     * @param title      chart title
     * @param tickSeries the related TickSeries
     */
    public void buildDataset(String title, TickSeries... tickSeries) {
        for (TickSeries series : tickSeries) {
            XYSeries timeSeries = new XYSeries(title);
            for (Tick tick : series.getTicks()) {
                timeSeries.addOrUpdate(tick.time().toEpochSecond(), tick.price());
            }
            tickSeriesDataset.addSeries(timeSeries);
        }
    }

    /**
     * Create and display the chart
     */
    public void buildChart() {

        // Generate TickSeries chart
        XYPlot tickSeriesPlot = new XYPlot();
        tickSeriesPlot.setDataset(tickSeriesDataset);
        tickSeriesPlot.setRenderer(new DefaultXYItemRenderer());

        NumberAxis seriesPriceAxis = new NumberAxis("Price");
        seriesPriceAxis.setAutoRangeIncludesZero(false);

        NumberAxis seriesDomainAxis = new NumberAxis("Time (epoch second)");
        seriesDomainAxis.setAutoRangeIncludesZero(false);

        tickSeriesPlot.setRangeAxis(seriesPriceAxis);
        tickSeriesPlot.setDomainAxis(seriesDomainAxis);

        JFreeChart seriesChart = new JFreeChart(getTitle(), tickSeriesPlot);
        ChartPanel seriesChartPanel = new ChartPanel(seriesChart);
        seriesChartPanel.setPreferredSize(new Dimension(800, 600));

        // Generate TickDistribution chart
        XYPlot distributionPlot = new XYPlot();
        distributionPlot.setDataset(orderbookDataset);
        XYBarRenderer distributionRenderer = new XYBarRenderer(0.575);

        distributionRenderer.setBarPainter(new StandardXYBarPainter() {
                @Override
                public void paintBarShadow(Graphics2D g2, XYBarRenderer renderer, int row,
                    int column, RectangularShape bar, RectangleEdge base,
                    boolean pegShadow){}
        });
        
        distributionRenderer.setSeriesPaint(0, new Color(50, 75, 100));
        distributionPlot.setRenderer(distributionRenderer);

        distributionPlot.setOrientation(PlotOrientation.HORIZONTAL);

        NumberAxis distributionValueAxis = new NumberAxis("Volume");
        distributionValueAxis.setAutoRangeIncludesZero(false);

        NumberAxis distributionDomainAxis = new NumberAxis("Price");
        distributionDomainAxis.setAutoRangeIncludesZero(false);

        distributionPlot.setRangeAxis(distributionValueAxis);
        distributionPlot.setDomainAxis(distributionDomainAxis);

        JFreeChart distributionChart = new JFreeChart("Distribution", distributionPlot);
        ChartPanel distributionChartPanel = new ChartPanel(distributionChart);
        distributionChartPanel.setPreferredSize(new Dimension(600, 600));

        JPanel panel = new JPanel();
        panel.add(seriesChartPanel);
        panel.add(distributionChartPanel);
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);   
    }
    
    /**
     * Add a Tickseries to the timeseries dataset of the chart
     * 
     * @param tickSeries the TickSeries
     */
    public void addTickSeries(TickSeries tickSeries) {
        tickSeries.addChangeListener(this);
        tickSeriesDataset.addSeries(new XYSeries(tickSeries.getName()));
    }

    public void addDistribution(TickDistribution distribution) {
        this.distribution = distribution;
        orderbookDataset.addSeries(new XYSeries(distribution.getName()));
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof TickSeries) {
            TickSeries source = (TickSeries) e.getSource();
            double time = source.lastTick().time().toInstant().toEpochMilli();
            tickSeriesDataset.getSeries(source.getName()).addOrUpdate(time, source.lastTick().price());
            for (double level : distribution.getLevels().keySet()) {
                orderbookDataset.getSeries(distribution.getName()).addOrUpdate(level, distribution.getVolumeAtPrice(level));
            }
        }
    }
}