package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Implimentation implements ChangeListener {

    private final LimitOrderBook orderBook;
    private final OrderManager orders;
    private final AvellanedaStoikovModel model;
    private final Position position; 
    private int count;

    public Implimentation(TradeSeries tradeSeries, LimitOrderBook orderBook, OrderManager orders, Position position, AvellanedaStoikovModel model) {
        listen(tradeSeries);
        this.orderBook = orderBook;
        this.orders = orders;
        this.model = model;
        this.position = position;
        this.count = 0;
    }

    private void listen(TradeSeries tradeSeries) {
        tradeSeries.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // tradeSeries tradeSeries = (tradeSeries) e.getSource();
        // long start = System.currentTimeMillis();
        count++;
        if (orderBook.isReady()) {
            if (count >= 5) {
                orders.update(
                    Math.min(model.bidPrice(position.signedValue()), orderBook.bestBid()),
                    Math.max(model.askPrice(position.signedValue()), orderBook.bestAsk())
                );
                count = 0;
            }
        }
        // System.out.println(System.currentTimeMillis() - start + " ms");
    }
}
