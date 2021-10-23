package com.metsuengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.RectangularShape;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.math3.ode.ODEIntegrator;
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
 
    /**
     * Constructor
     * 
     * @param title the title of the timeseries chart
     */
    public Chart(String title, TickSeries tickSeries, MarketOrderBook orderBook) {
        super(title);
        this.tickSeriesDataset = new XYSeriesCollection();
        this.orderbookDataset = new XYSeriesCollection();
        tickSeries.addChangeListener(this);
        tickSeriesDataset.addSeries(new XYSeries(tickSeries.getName()));
        orderBook.addChangeListener(this);
        orderbookDataset.addSeries(new XYSeries(orderBook.getName()));
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
        XYPlot orderBookPlot = new XYPlot();
        orderBookPlot.setDataset(orderbookDataset);
        XYBarRenderer orderBookRenderer = new XYBarRenderer(0.575);

        orderBookRenderer.setBarPainter(new StandardXYBarPainter() {
                @Override
                public void paintBarShadow(Graphics2D g2, XYBarRenderer renderer, int row,
                    int column, RectangularShape bar, RectangleEdge base,
                    boolean pegShadow){}
        });
        
        orderBookRenderer.setSeriesPaint(0, new Color(50, 75, 100));
        orderBookPlot.setRenderer(orderBookRenderer);

        orderBookPlot.setOrientation(PlotOrientation.HORIZONTAL);

        NumberAxis orderBookValueAxis = new NumberAxis("Volume");
        orderBookValueAxis.setAutoRangeIncludesZero(false);

        NumberAxis orderBookDomainAxis = new NumberAxis("Price");
        orderBookDomainAxis.setAutoRangeIncludesZero(false);

        orderBookPlot.setRangeAxis(orderBookValueAxis);
        orderBookPlot.setDomainAxis(orderBookDomainAxis);

        JFreeChart distributionChart = new JFreeChart("Distribution", orderBookPlot);
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
    
    
    
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof TickSeries) {
            TickSeries source = (TickSeries) e.getSource();
            double time = source.lastTick().time().toInstant().toEpochMilli();
            tickSeriesDataset.getSeries(source.getName()).addOrUpdate(time, source.lastTick().price());
        } else if (e.getSource() instanceof MarketOrderBook) {
            MarketOrderBook source = (MarketOrderBook) e.getSource();
            for (double price : source.orderBook().keySet()) {
                double value = (double) source.orderBook().get(price);
                orderbookDataset.getSeries(source.getName()).addOrUpdate(price, value);
            }
        }
    }
}