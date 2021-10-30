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

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class OrderMatchingStrategy implements ChangeListener {

    private static final Logger LOGGER = Logger.getLogger(OrderMatchingStrategy.class.getName());
    private final BybitRestAPIClient api;
    private final LimitOrderBook orderBook;
    private final DescriptiveStatistics ds = new DescriptiveStatistics();
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

                // if orders contains a bid
                if (quotes.bid().isPresent()) {
                    // if current bid is not the name as the new calculated bid
                    if (quotes.bid().get().isEquivalentTo(newBid)) {
                        api.replaceOrder(quotes.bid().get().orderId(), newBid);
                    }
                } else {
                    api.placeOrder(newBid);
                }

                // if orders contains an ask
                if (quotes.ask().isPresent()) {
                    // if current ask is not the name as the new calculated bid
                    if (quotes.ask().get().isEquivalentTo(newAsk)) {
                        api.replaceOrder(quotes.ask().get().orderId(), newAsk);
                    }
                } else {
                    api.placeOrder(newAsk);
                }

            } catch (IOException | InvalidKeyException | NoSuchAlgorithmException ex) {
                LOGGER.log(Level.SEVERE, "CANCELLING ALL ORDERS", ex);
                api.cancellAllOrders();
            }    
        }
        long end = System.currentTimeMillis();
        ds.addValue(end - start);
        // System.out.println(ds.getMean());
    }    
}
