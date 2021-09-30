package com.metsuengine;

import java.util.ArrayList;
import java.util.List;

public class ChaseOrder extends Order implements Runnable{

    private double lastTradedPrice;
    private double currentEntry;
    private double targetEntry;
    private double targetExit;
    private List<Order> orders;
    
    public ChaseOrder(double lastTradedPrice, double target, int qty) {
        super(target,  qty);
        this.lastTradedPrice = lastTradedPrice;
        this.orders = new ArrayList<Order>();
    }

    public void submitOrder(double price, int qty) {
        orders.add(new Order(price, qty));
    }

    @Override
    public void run() {
        while (true) {
            if (lastTradedPrice < targetEntry) {
                if (currentEntry != targetEntry) {

                    // https
                    this.currentEntry = targetEntry;
                }
            } else if (lastTradedPrice > targetEntry) {
                // uuuuugh
            }
        }
    }
}
