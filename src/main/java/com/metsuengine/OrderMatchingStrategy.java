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

public class OrderMatchingStrategy implements ChangeListener {

    private static final Logger LOGGER = Logger.getLogger(OrderMatchingStrategy.class.getName());
    private BybitRestAPIClient api;
    private MarketOrderBook orderBook;

    public OrderMatchingStrategy(TickSeries tickSeries, MarketOrderBook orderBook) {
        tickSeries.addChangeListener(this);
        this.orderBook = orderBook;
        this.api = new BybitRestAPIClient("BTCUSD");
    }

    @Override
    public void stateChanged(ChangeEvent e) {
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
                        System.out.println("Replacing bid " + currentBid.orderId() + " with " + newBid.price() + " × " + newBid.price());
                        api.replaceOrder(currentBid.orderId(), newBid);
                    }
                } else {
                    System.out.println("Placing bid at " + newBid.price());
                    api.placeOrder(newBid);
                }

                // if orders contains an ask
                Optional<Order> optionalAsk = orders.stream().filter(o -> o.side().equals(Side.Sell)).findAny();
                if (optionalAsk.isPresent()) {
                    Order currentAsk = optionalAsk.get();
                    // if current ask is not the name as the new calculated bid
                    if (!currentAsk.isEquivalentTo(newAsk)) {
                        System.out.println("Replacing ask " + currentAsk.orderId() + " with " + newAsk.price() + " × " + newAsk.price());
                        api.replaceOrder(currentAsk.orderId(), newAsk);
                    }
                } else {
                    System.out.println("Placing ask at " + newAsk.price());
                    api.placeOrder(newAsk);
                }

            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "CANCELLING ALL ORDERS", ex);
                api.cancellAllOrders();
            }    
        }
    }    
}
