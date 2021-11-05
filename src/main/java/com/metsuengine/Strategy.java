package com.metsuengine;

import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Strategy implements ChangeListener {

    private static final Logger LOGGER = Logger.getLogger(Strategy.class.getName());
    private final BybitRestAPIClient api;
    private final LimitOrderBook orderBook;
    private final OrderManager orders;
    private final Model model;
    private final Position position; 

    public Strategy(TradeSeries tradeSeries, LimitOrderBook orderBook, OrderManager orders, Position position, Model model) {
        listen(tradeSeries);
        this.orderBook = orderBook;
        this.api = new BybitRestAPIClient("BTCUSD");
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
        long start = System.currentTimeMillis();
        if (orderBook.isReady()) {

            // TODO 
            
        }
        System.out.println(System.currentTimeMillis() - start + "ms");
    }
}
