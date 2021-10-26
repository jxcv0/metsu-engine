package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Controller implements ChangeListener {

    // private boolean inPosition;
    private CSVManager csv;
    private int tempCount;

    public Controller(MarketOrderBook orderBook) {
        orderBook.addChangeListener(this);
        // this.inPosition = false;
        this.csv = new CSVManager("src\\main\\resources\\log.csv");
        this.tempCount = 0;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        tempCount++;
        MarketOrderBook orderBook = (MarketOrderBook) e.getSource();
        if (orderBook.isReady() && tempCount >= 10000) {

            String[] line = {
                Double.toString(orderBook.bestBid()),
                Double.toString(orderBook.delta(0.01))};

            csv.writeLine(line);
            tempCount = 0;
        }
    }
}
