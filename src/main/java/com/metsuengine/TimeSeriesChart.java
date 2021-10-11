package com.metsuengine;

import java.awt.Color;
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
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.Position;
import org.ta4j.core.Strategy;
import org.ta4j.core.Trade.TradeType;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

public class TimeSeriesChart extends ApplicationFrame implements ChangeListener {

    private final JFreeChart chart;
    private final TimeSeriesCollection dataset;
    private final List<Marker> markers;
    private TimeSeries timeSeries;

    /**
     * Constructor
     * 
     * @param title the title of the timeseries chart
     */
    public TimeSeriesChart(String title) {
        super(title);
        this.timeSeries = new TimeSeries("Tick Data");
        this.dataset = new TimeSeriesCollection(this.timeSeries);
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
    public void buildDataset(String title, TickSeries tickSeries) {
        TimeSeries timeSeries = new TimeSeries(title);
        for (Tick tick : tickSeries.getTicks()) {
            timeSeries.addOrUpdate(new Millisecond(Date.from(tick.time().toInstant())), tick.price());
        }
        dataset.addSeries(timeSeries);
    }

    public void buildDataset(String title, BarSeries barSeries) {
        TimeSeries timeSeries = new TimeSeries(title);
        
        for (Bar bar : barSeries.getBarData()) {
            timeSeries.addOrUpdate(new Minute(Date.from(bar.getEndTime().toInstant())), bar.getClosePrice().doubleValue());
        }
        dataset.addSeries(timeSeries);
    }

    public void buildDataset(String title, BarSeries barSeries, Indicator<Num> indicator) {
        TimeSeries timeSeries = new TimeSeries(title);
        for (int i = 0; i < barSeries.getBarCount(); i++) {
            Bar bar = barSeries.getBar(i);
            timeSeries.addOrUpdate(new Minute(Date.from(bar.getEndTime().toInstant())), indicator.getValue(i).doubleValue());
        }
        dataset.addSeries(timeSeries);
    }

    public void addMarkers(BarSeries barSeries, TradingRecord tradingRecord, Strategy strategy) {
        List<Position> positions = tradingRecord.getPositions();
        if (tradingRecord.getStartingType().equals(TradeType.SELL)) {
            for (Position position : positions) {

                double entrySignalTime = new Minute(Date.from(barSeries.getBar(position.getEntry().getIndex()).getEndTime().toInstant())).getFirstMillisecond();
                Marker entryMarker = new ValueMarker(entrySignalTime);
                entryMarker.setPaint(Color.RED);
                entryMarker.setLabel(strategy.getName() + " SHORT ENTRY");
                entryMarker.setLabelAnchor(RectangleAnchor.CENTER);
                markers.add(entryMarker);
    
                double exitSignalTime = new Minute(Date.from(barSeries.getBar(position.getExit().getIndex()).getEndTime().toInstant())).getFirstMillisecond();
                Marker exitMarker = new ValueMarker(exitSignalTime);
                exitMarker.setPaint(Color.GREEN);
                exitMarker.setLabel(strategy.getName() + " SHORT EXIT");
                exitMarker.setLabelAnchor(RectangleAnchor.CENTER);
                markers.add(exitMarker);
            }  
        } else {
            for (Position position : positions) {

                double buySignalTime = new Minute(Date.from(barSeries.getBar(position.getEntry().getIndex()).getEndTime().toInstant())).getFirstMillisecond();
                Marker entryMarker = new ValueMarker(buySignalTime);
                entryMarker.setPaint(Color.GREEN);
                entryMarker.setLabel(strategy.getName() + " LONG ENTRY");
                entryMarker.setLabelAnchor(RectangleAnchor.CENTER);
                markers.add(entryMarker);
    
                double sellSignalTime = new Minute(Date.from(barSeries.getBar(position.getExit().getIndex()).getEndTime().toInstant())).getFirstMillisecond();
                Marker exitMarker = new ValueMarker(sellSignalTime);
                exitMarker.setPaint(Color.RED);
                exitMarker.setLabel(strategy.getName() + " LONG EXIT");
                exitMarker.setLabelAnchor(RectangleAnchor.CENTER);
                markers.add(exitMarker);
            }
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

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        this.timeSeries.addOrUpdate(new Millisecond(Date.from(source.getLastTick().time().toInstant())), source.getLastTick().price());
    }
}
