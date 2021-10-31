package com.metsuengine;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.metsuengine.Enums.OrderType;
import com.metsuengine.Enums.Side;
import com.metsuengine.Enums.TimeInForce;

public class OrderMatchingStrategy implements ChangeListener {

    private static final Logger LOGGER = Logger.getLogger(OrderMatchingStrategy.class.getName());
    private final BybitRestAPIClient api;
    private final LimitOrderBook orderBook;
    private final Order newBid;
    private final Order newAsk;
    private final QuotePair quotes;

    public OrderMatchingStrategy(TickSeries tickSeries, LimitOrderBook orderBook, QuotePair quotes) {
        listen(tickSeries);
        this.orderBook = orderBook;
        this.api = new BybitRestAPIClient("BTCUSD");
        this.newBid = new Order("BTCUSD", Side.Buy, OrderType.Limit, TimeInForce.GoodTillCancel);
        this.newAsk = new Order("BTCUSD", Side.Sell, OrderType.Limit, TimeInForce.GoodTillCancel);
        this.quotes = quotes;
    }

    private void listen(TickSeries tickSeries) {
        tickSeries.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        long start = System.currentTimeMillis();
        if (orderBook.isReady()) {
            try {
                newBid.updatePrice(orderBook.bestBid());
                newBid.updateQty(1);

                newAsk.updatePrice(orderBook.bestAsk());
                newAsk.updateQty(1);

                if (quotes.bid().isPresent()) {
                    api.replaceOrder(quotes.bid().get().orderId(), newBid);
                } else {
                    api.placeOrder(newBid);
                }

                if (quotes.ask().isPresent()) {
                    api.replaceOrder(quotes.ask().get().orderId(), newAsk);
                } else {
                    api.placeOrder(newAsk);
                }

            } catch (IOException | InvalidKeyException | NoSuchAlgorithmException ex) {
                LOGGER.log(Level.SEVERE, "CANCELLING ALL ORDERS", ex);
                api.cancellAllOrders();
            }    
        }
        System.out.println(System.currentTimeMillis() - start);
    }    
}
