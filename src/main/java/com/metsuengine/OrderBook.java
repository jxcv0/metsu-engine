package com.metsuengine;

import java.util.HashMap;
import java.util.logging.Logger;

public class OrderBook extends HashMap<Double, Order>{

    private static final Logger LOGGER = Logger.getLogger(OrderBook.class.getName());
    
    public OrderBook() {

    }

    public void addOrder(Order order) {
        this.put(order.limit(), order);
    }

    public Order getOrder(double limit) {
        return this.get(limit);
    }

    public void update() {
        for (double level : this.keySet()) {
            if (level != this.get(level).limit()) {
                // TODO update level through api
                Order order = this.get(level);

                LOGGER.info("Updating limit order from " + level + " to " + order.limit());
                
                this.remove(level);
                this.addOrder(order);
            }
        }
    }
}
