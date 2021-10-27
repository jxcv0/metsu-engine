package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Controller implements ChangeListener {

    private boolean inPosition;
    private CSVManager csv;
    private int count;
    private final int frequency;
    private final double range;

    public Controller(MarketOrderBook orderBook, int frequency, double range, double threshold) {
        orderBook.addChangeListener(this);
        this.inPosition = false;
        this.csv = new CSVManager("src\\main\\resources\\log.csv");
        this.frequency = frequency;
        this.range = range;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        count++;
        if (count >= frequency) {
            MarketOrderBook orderBook = (MarketOrderBook) e.getSource();
            if (orderBook.isReady()) {
                // if orderBook delta is above threshold && no long orders are placed
                    // create ladder down to (bestbid - range).
                    
                count = 0;
            }
        }
    }
}
