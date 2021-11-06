package com.metsuengine;

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
        // TODO
    }
}
