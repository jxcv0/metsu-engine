package com.metsuengine;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class TimeSeriesChart extends JFrame implements ChangeListener {
    
    private final XYSeriesCollection timeSeriesDataset;
    private final XYSeriesCollection distributionDataset;
 
    /**
     * Constructor
     * 
     * @param title the title of the timeseries chart
     */
    public TimeSeriesChart(String title) {
        super(title);
        this.timeSeriesDataset = new XYSeriesCollection();
        this.distributionDataset = new XYSeriesCollection();
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
            timeSeriesDataset.addSeries(timeSeries);
        }
    }

    /**
     * Create and display the chart
     */
    public void displayChart() {
        
        XYPlot plot = new XYPlot();
        plot.setDataset(0, timeSeriesDataset);
        plot.setDataset(1, distributionDataset);
        
        XYBarRenderer barRenderer = new XYBarRenderer();
        plot.setRenderer(0, new DefaultXYItemRenderer());
        plot.setRenderer(1, barRenderer);

        NumberAxis priceAxis = new NumberAxis("Price");
        priceAxis.setAutoRange(true);
        priceAxis.setAutoRangeIncludesZero(false);
        NumberAxis valueAxis = new NumberAxis();
        valueAxis.setAutoRange(true);
        valueAxis.setAutoRangeIncludesZero(false);
        NumberAxis domainAxis = new NumberAxis("Time (epoch second)");
        domainAxis.setAutoRangeIncludesZero(false);

        plot.setRangeAxis(0, priceAxis);
        plot.setRangeAxis(1, valueAxis);
        plot.setDomainAxis(domainAxis);
        plot.mapDatasetToRangeAxis(0, 0);
        plot.mapDatasetToRangeAxis(1, 1);

        JFreeChart chart = new JFreeChart(getTitle(), plot);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setPreferredSize(new Dimension(1000, 600));

        JPanel panel = new JPanel();
        panel.add(chartPanel);
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
        timeSeriesDataset.addSeries(new XYSeries(tickSeries.getName()));
    }

    public void addDistribution(TickDistribution tickDistribution) {
        distributionDataset.addSeries(new XYSeries(tickDistribution.getName()));
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof TickSeries) {
            TickSeries source = (TickSeries) e.getSource();
            double time = source.getLastTick().time().toInstant().toEpochMilli();
            timeSeriesDataset.getSeries(source.getName()).addOrUpdate(time, source.getLastTick().price()); 
        }
    }
}
