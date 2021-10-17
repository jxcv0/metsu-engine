package com.metsuengine;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class TimeSeriesChart extends JFrame implements ChangeListener {
    
    private final XYSeriesCollection timeSeriesDataset;
    private final XYSeriesCollection alternativeDataset;
    private BetaCoefficient beta;
    private boolean flag;
 
    /**
     * Constructor
     * 
     * @param title the title of the timeseries chart
     */
    public TimeSeriesChart(String title, boolean flag) {
        super(title);
        this.timeSeriesDataset = new XYSeriesCollection();
        this.alternativeDataset = new XYSeriesCollection();
        this.flag = flag;   
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
        plot.setDataset(1, alternativeDataset);

        plot.setRenderer(0, new DefaultXYItemRenderer());
        plot.setRenderer(1, new DefaultXYItemRenderer());

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
        chart.removeLegend();

        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setPreferredSize(new Dimension(1000, 600));

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

    public void addToAlternativeDataset(BetaCoefficient beta) {
        this.beta = beta;
        alternativeDataset.addSeries(new XYSeries(beta.getName()));
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        double time = source.getLastTick().time().toInstant().toEpochMilli();
        if (flag) {
            timeSeriesDataset.getSeries(source.getName()).addOrUpdate(time, source.getLastTick().price()); 
        }
        if (beta.getValue() != 0) {
            alternativeDataset.getSeries(beta.getName()).addOrUpdate(time, beta.getValue());
        }
    }
}
