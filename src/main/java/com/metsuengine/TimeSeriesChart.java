package com.metsuengine;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class TimeSeriesChart extends JFrame implements ChangeListener {
    
    private final XYSeriesCollection timeSeriesDataset;
    private final XYSeriesCollection indicatorDataset;
    private final List<Indicator> indicators;
    private final List<Marker> markers;

    /**
     * Constructor
     * 
     * @param title the title of the timeseries chart
     */
    public TimeSeriesChart(String title) {
        super(title);
        this.timeSeriesDataset = new XYSeriesCollection();
        this.indicatorDataset = new XYSeriesCollection();
        this.indicators = new ArrayList<Indicator>();
        this.markers = new ArrayList<Marker>();
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
 
    public void displayChart() {
        
        XYPlot plot = new XYPlot();
        plot.setDataset(0, timeSeriesDataset);
        plot.setDataset(1, indicatorDataset);

        plot.setRenderer(0, new XYLineAndShapeRenderer());
        plot.setRenderer(1, new XYLineAndShapeRenderer());

        NumberAxis priceAxis = new NumberAxis("Price");
        priceAxis.setAutoRange(true);
        priceAxis.setAutoRangeIncludesZero(false);
        NumberAxis valueAxis = new NumberAxis("Indicator");
        valueAxis.setAutoRange(true);
        valueAxis.setAutoRangeIncludesZero(false);
        NumberAxis domainAxis = new NumberAxis("Time (epoch second)");
        domainAxis.setFixedAutoRange(100000);
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
        panel.setPreferredSize(new Dimension(800, 500));

        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void addTickSeries(TickSeries tickSeries) {
        tickSeries.addChangeListener(this);
        timeSeriesDataset.addSeries(new XYSeries(tickSeries.getName()));
    }

    public void addIndicator(Indicator indicator) {
        indicators.add(indicator);
        indicatorDataset.addSeries(new XYSeries(indicator.getName()));
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        double time = source.getLastTick().time().toInstant().toEpochMilli();
        timeSeriesDataset.getSeries(source.getName()).addOrUpdate(time, source.getLastTick().price());
        for (Indicator indicator : indicators) {
            indicatorDataset.getSeries(indicator.getName()).addOrUpdate(time, indicator.getValue());
        }
    }
}
