package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Controller implements ChangeListener {

    private final MarketOrderBook orderBook;

    public Controller(TickSeries tickSeries, MarketOrderBook orderBook) {
        tickSeries.addChangeListener(this);
        this.orderBook = orderBook;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        double marketDelta = source.deltaRatio();
        double limitDelta = orderBook.deltaRatio();
        double deltaMetric = (marketDelta + limitDelta);
        System.out.println(source.lastTick().price() + " " + deltaMetric + " " + source.size());
    }
}
