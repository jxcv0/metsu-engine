package com.metsuengine;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
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

                System.out.println(quotes.bid().isPresent());

                // if orders contains a bid - SLOOOOOOOOWWWWWWWWW
                if (quotes.bid().isPresent()) {
                    Order currentBid = quotes.bid().get();
                    // if current bid is not the name as the new calculated bid
                    if (!currentBid.isEquivalentTo(newBid)) {
                        api.replaceOrder(currentBid.orderId(), newBid);
                    }
                } else {
                    api.placeOrder(newBid);
                }

                // if orders contains an ask - ALSO SLOOOOOOOOOOWWWWWWWWW
                if (quotes.ask().isPresent()) {
                    Order currentAsk = quotes.ask().get();
                    // if current ask is not the name as the new calculated bid
                    if (!currentAsk.isEquivalentTo(newAsk)) {
                        api.replaceOrder(currentAsk.orderId(), newAsk);
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
