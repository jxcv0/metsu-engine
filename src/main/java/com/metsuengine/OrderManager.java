package com.metsuengine;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.metsuengine.Enums.OrderType;
import com.metsuengine.Enums.Side;
import com.metsuengine.Enums.TimeInForce;

public class OrderManager {
    
    private final String symbol;
    private static final Logger LOGGER = Logger.getLogger(OrderManager.class.getName());
    private final Map<String, Order> orders;
    private final BybitAPIClient api;

    public OrderManager(String symbol, BybitAPIClient api) {
        this.symbol = symbol;
        this.orders = new ConcurrentHashMap<>();
        this.api = api;
    }

    /**
     * Either adds or removes an order depending on OrderState of order recieved through websocket
     * @param order the order to proccess
     */
    public void proccessOrder(Order order) {
        switch (order.orderStatus()) {
            case New:
                LOGGER.info("Order " + order.orderId() + " is new");
                orders.put(order.orderId(), order);
                break;
            
            case Created:
                LOGGER.info("Order " + order.orderId() + " created");
                orders.put(order.orderId(), order);
                break;
            
            case PartiallyFilled:
                LOGGER.info("Order " + order.orderId() + " partially filled");
                orders.put(order.orderId(), order);
                break;
        
            default:
                LOGGER.info("Order " + order.orderId() + " cancelled");
                orders.remove(order.orderId());
                break;
        }
        System.out.println(orders.size());
    }

    /**
     * Check if there is an order at a price
     * @param price the price to check at
     * @return true if order is present
     */
    public boolean orderAtPrice(double price) {
        return orders.values().stream().anyMatch(o -> o.price() == price);
    }
    

    /**
     * Manages orders based on bid prices supplied by model
     * @param bidPrice
     * @param askPrice
     */
    public void update(double bidPrice, double askPrice) {
        // TODO - make readable
        try {
            if (bids().size() > 0) {
                if (bids().size() > 1) {
                    bids().forEach(o -> {
                        try {
                            api.cancelOrder(o.orderId());
                        } catch (InvalidKeyException | NoSuchAlgorithmException | IOException e) {
                            LOGGER.log(Level.SEVERE, "Unable to cancel Buy orders", e);
                        }
                    });
                    api.placeOrder(symbol, Side.Buy, OrderType.Limit, bidPrice, 1, TimeInForce.GoodTillCancel);
                } else {
                    bids().stream().findAny().ifPresent(o -> {
                        if (o.price() != bidPrice) {
                            try {
                                api.replaceOrder(o.orderId(), symbol, bidPrice, 1);
                            } catch (InvalidKeyException | NoSuchAlgorithmException | IOException e) {
                                LOGGER.log(Level.SEVERE, "Unable to replace Buy order", e);
                            }
                        }
                    });
                }
            } else {
                api.placeOrder(symbol, Side.Buy, OrderType.Limit, bidPrice, 1, TimeInForce.GoodTillCancel);
            }

            if (asks().size() > 0) {
                if (asks().size() > 1) {
                    asks().forEach(o -> {
                        try {
                            api.cancelOrder(o.orderId());
                        } catch (InvalidKeyException | NoSuchAlgorithmException | IOException e) {
                            LOGGER.log(Level.SEVERE, "Unable to cancel Sell orders", e);
                        }
                    });
                    api.placeOrder(symbol, Side.Sell, OrderType.Limit, askPrice, 1, TimeInForce.GoodTillCancel);
                } else {
                    asks().stream().findAny().ifPresent(o -> {
                        if (o.price() != askPrice) {
                            try {
                                api.replaceOrder(o.orderId(), symbol, askPrice, 1);
                            } catch (InvalidKeyException | NoSuchAlgorithmException | IOException e) {
                                LOGGER.log(Level.SEVERE, "Unable to replace Buy order", e);
                            }
                        }
                    });
                }
            } else {
                api.placeOrder(symbol, Side.Sell, OrderType.Limit, askPrice, 1, TimeInForce.GoodTillCancel);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while managing orders", e);
        }        
    }

    public List<Order> bids() {
        return orders.values().stream().filter(o -> o.isBuy()).toList();
    }

    public List<Order> asks() {
        return orders.values().stream().filter(o -> !o.isBuy()).toList();
    }
}
