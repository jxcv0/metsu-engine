package com.metsuengine;

public class Order {

    private Side side;
    private double price;
    private int qty;
    
    public enum Side {
        LONG,
        SHORT
    }

    public Order(Side side, double price, int qty) {
        this.side = side;
        this.price = price;
        this.qty = qty;
    }
}
