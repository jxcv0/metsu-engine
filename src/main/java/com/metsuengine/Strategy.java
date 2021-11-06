package com.metsuengine;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Strategy implements ChangeListener {

    private final LimitOrderBook orderBook;
    private final OrderManager orders;
    private final Model model;
    private final Position position; 

    public Strategy(TradeSeries tradeSeries, LimitOrderBook orderBook, OrderManager orders, Position position, Model model) {
        listen(tradeSeries);
        this.orderBook = orderBook;
        this.orders = orders;
        this.model = model;
        this.position = position;
    }

    private void listen(TradeSeries tradeSeries) {
        tradeSeries.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // tradeSeries tradeSeries = (tradeSeries) e.getSource();
        // long start = System.currentTimeMillis();
        if (orderBook.isReady()) {
            orders.update(
                Math.min(model.bidPrice(position.signedValue()), orderBook.bestBid()),
                Math.max(model.askPrice(position.signedValue()), orderBook.bestAsk())
            );
        }
        // System.out.println(System.currentTimeMillis() - start + " ms");
    }
}
