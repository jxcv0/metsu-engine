package com.metsuengine;

public class Order {

    private Side side;
    private double price;
    private int qty;
    private boolean filled;
    
    public enum Side {
        LONG,
        SHORT
    }

    public Order(Side side, double price, int qty) {
        this.side = side;
        this.price = price;
        this.qty = qty;
        this.filled = false;
    }

    public Side side() {
        return this.side;
    }

    public double price() {
        return this.price;
    }

    public void amendPrice(double price) {
        if (!this.isFilled()) {
            this.price = price;
        }
    }

    public int qty() {
        return this.qty;
    }

    public boolean isFilled() {
        return this.filled;
    }
}
