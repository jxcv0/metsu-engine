package com.metsuengine;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.metsuengine.Enums.OrderStatus;
import com.metsuengine.Enums.OrderType;
import com.metsuengine.Enums.Side;
import com.metsuengine.Enums.TimeInForce;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class OrderMatchingStrategy implements ChangeListener {

    private static final Logger LOGGER = Logger.getLogger(OrderMatchingStrategy.class.getName());
    private final BybitRestAPIClient api;
    private final MarketOrderBook orderBook;
    private final DescriptiveStatistics ds = new DescriptiveStatistics();

    public OrderMatchingStrategy(TickSeries tickSeries, MarketOrderBook orderBook) {
        extracted(tickSeries);
        this.orderBook = orderBook;
        this.api = new BybitRestAPIClient("BTCUSD");
    }

    private void extracted(TickSeries tickSeries) {
        tickSeries.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        long start = System.currentTimeMillis();
        if (orderBook.isReady()) {
            try {
                double bidPrice = orderBook.bestBid();
                double askPrice = orderBook.bestAsk();
                
                Order newBid = new Order("BTCUSD", Side.Buy, OrderType.Limit, bidPrice, 1, TimeInForce.GoodTillCancel, OrderStatus.New);
                Order newAsk = new Order("BTCUSD", Side.Sell, OrderType.Limit, askPrice, 1, TimeInForce.GoodTillCancel, OrderStatus.New);

                List<Order> orders = api.getOrders();

                // if orders contains a bid
                Optional<Order> optionalBid = orders.stream().filter(o -> o.side().equals(Side.Buy)).findAny();
                if (optionalBid.isPresent()) {
                    Order currentBid = optionalBid.get();
                    // if current bid is not the name as the new calculated bid
                    if (!currentBid.isEquivalentTo(newBid)) {
                        api.replaceOrder(currentBid.orderId(), newBid);
                    }
                } else {
                    api.placeOrder(newBid);
                }

                // if orders contains an ask
                Optional<Order> optionalAsk = orders.stream().filter(o -> o.side().equals(Side.Sell)).findAny();
                if (optionalAsk.isPresent()) {
                    Order currentAsk = optionalAsk.get();
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
        System.out.println(ds.getMean());
    }    
}
