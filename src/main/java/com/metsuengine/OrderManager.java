package com.metsuengine;

import java.util.HashMap;
import java.util.Map;

public class OrderManager {
    
    private final Map<String, Order> buyOrders;
    private final Map<String, Order> sellOrders;

    public OrderManager() {
        this.buyOrders = new HashMap<>();
        this.sellOrders = new HashMap<>();
    }

    /**
     * Either adds, removes or replaces an order depending on OrderState
     * @param order the order
     */
    public void proccessOrder(Order order) {
        if (order.isBuy()) {
            switch (order.orderStatus()) {
                case New:
                    buyOrders.put(order.orderId(), order);
                    break;
                
                case Created:
                    buyOrders.put(order.orderId(), order);
                    break;
                
                case PartiallyFilled:
                    buyOrders.put(order.orderId(), order);
                    break;
            
                default:
                    buyOrders.remove(order.orderId());
                    break;
            }
        } else {
            switch (order.orderStatus()) {
                case New:
                    sellOrders.put(order.orderId(), order);
                    break;
                
                case Created:
                    sellOrders.put(order.orderId(), order);
                    break;
                
                case PartiallyFilled:
                    sellOrders.put(order.orderId(), order);
                    break;
            
                default:
                    sellOrders.remove(order.orderId());
                    break;
            }
        }
    }

    /**
     * Checks if a new order is equivalent (not equal) to an existing order
     * @param order the order
     * @return boolean result
     */
    public boolean hasEquivalentBid(Order order) {
        return buyOrders.values().stream().anyMatch(o -> o.price() == order.price()) ? true : false;
    }
}
