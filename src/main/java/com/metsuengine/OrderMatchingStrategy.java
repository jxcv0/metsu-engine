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
                newBid.updateQty(100);

                newAsk.updatePrice(orderBook.bestAsk());
                newAsk.updateQty(100);

                switch (quotes.state()) {
                    case HasBoth:
                        compareBid();
                        compareAsk();
                        break;
                    
                    case HasBid:
                        compareBid();
                        api.placeOrder(newAsk);
                        break;

                    case HasAsk:
                        compareAsk();
                        api.placeOrder(newBid);
                        break;

                    default:
                        api.placeOrder(newBid);
                        api.placeOrder(newAsk);
                        break;
                }

            } catch (IOException | InvalidKeyException | NoSuchAlgorithmException ex) {
                LOGGER.log(Level.SEVERE, "CANCELLING ALL ORDERS", ex);
                api.cancellAllOrders();
            }    
        }
        System.out.println(System.currentTimeMillis() - start + "ms");
    }
    
    public void compareBid() {
        try {
            if (quotes.bid().get().price() != newBid.price()) {
                api.replaceOrder(quotes.bid().get().orderId(), newBid);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "CANCELLING ALL ORDERS", e);
            api.cancellAllOrders();
        }
    }

    public void compareAsk() {
        try {
            if (quotes.ask().get().price() != newAsk.price()) {
                api.replaceOrder(quotes.ask().get().orderId(), newAsk);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "CANCELLING ALL ORDERS", e);
            api.cancellAllOrders();
        }
    }
}
