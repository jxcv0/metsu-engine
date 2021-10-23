package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Controller implements ChangeListener {

    private final DescriptiveStatistics descStat;
    private final MarketOrderBook orderBook;

    public Controller(TickSeries tickSeries, MarketOrderBook orderBook) {
        this.descStat = new DescriptiveStatistics();
        tickSeries.addChangeListener(this);
        this.orderBook = orderBook;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        descStat.addValue(orderBook.delta());
        System.out.println(descStat.getMean());     
    }
}
