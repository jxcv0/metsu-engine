package com.metsuengine;

public class Order {
    
    protected double price;
    protected int qty;
    protected boolean filled;
    
    public Order(double price, int qty) {
        // https
        this.price = price;
        this.qty = qty;
    }

    public void updatePrice(double price) {
        // https
        this.price = price;
    }

    public double price() {
        return this.price;
    }

    public double qty() {
        return this.qty;
    }

    public void fill() {
        this.filled = true;
    }

    public boolean isFilled() {
        return filled;
    }
}
