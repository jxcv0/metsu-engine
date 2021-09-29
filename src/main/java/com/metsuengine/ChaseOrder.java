package com.metsuengine;

import java.util.ArrayList;
import java.util.List;

public class ChaseOrder extends Order implements Runnable {

    private double target;
    private List<Order> orders;
    
    public ChaseOrder(double target, int qty) {
        super(target,  qty);
        this.orders = new ArrayList<Order>();
    }

    public void submitOrder(double price, int qty) {
        orders.add(new Order(price, qty));
    }

    @Override
    public void run() {
        while(!filled) {
            if(price != target) {
                updatePrice(target);
                // TODO break if filled
            }
        } 
    }
}
