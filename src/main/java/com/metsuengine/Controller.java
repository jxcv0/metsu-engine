package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Controller implements ChangeListener {

    private final CSVManager manager;
    private final MarketOrderBook orderBook;

    public Controller(String filename, TickSeries tickSeries, MarketOrderBook orderBook) {
        this.manager = new CSVManager(filename);
        tickSeries.addChangeListener(this);
        this.orderBook = orderBook;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        String[] line = {source.lastTick().time().toString(),
            Double.toString(source.lastTick().price()),
            Double.toString(source.delta()),
            Double.toString(orderBook.deltaRatio())};
        manager.writeLine(line);
        System.out.println(source.size());
    }
}
