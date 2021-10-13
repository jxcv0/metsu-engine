package com.metsuengine;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class TimeSeriesChart extends ApplicationFrame implements ChangeListener {

    private final JFreeChart chart;
    private final TimeSeriesCollection dataset;
    private final List<Marker> markers;

    /**
     * Constructor
     * 
     * @param title the title of the timeseries chart
     */
    public TimeSeriesChart(String title) {
        super(title);
        this.dataset = new TimeSeriesCollection();
        this.markers = new ArrayList<Marker>();
        this.chart = ChartFactory.createTimeSeriesChart(title, "Time", "Price", dataset);
        chart.removeLegend();

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new java.awt.Dimension(1000, 500));
        setContentPane(panel);
    }
    
    /**
     * plots a Time-Price chart from a TickSeries
     * 
     * @param title      chart title
     * @param tickSeries the related TickSeries
     */
    public void buildDataset(String title, TickSeries... tickSeries) {
        for (TickSeries series : tickSeries) {
            TimeSeries timeSeries = new TimeSeries(title);
            for (Tick tick : series.getTicks()) {
                timeSeries.addOrUpdate(new Millisecond(Date.from(tick.time().toInstant())), tick.price());
            }
            dataset.addSeries(timeSeries);
        }
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
        for (Marker marker : markers) {
            plot.addDomainMarker(marker);
        }
    }

    public void addTickSeries(TickSeries tickSeries) {
        tickSeries.addChangeListener(this);
        dataset.addSeries(new TimeSeries(tickSeries.getname()));
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        dataset.getSeries(source.getname()).addOrUpdate(new Millisecond(Date.from(source.getLastTick().time().toInstant())), source.getLastTick().price());
    }
}
