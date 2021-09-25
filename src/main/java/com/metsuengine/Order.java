package com.metsuengine;

public class Order {

    private Side side;
    private double price;
    private int qty;
    private boolean filled;
    private String id;
    private OrderType type;
    
    public enum Side {
        Buy,
        Sell
    }

    // /v2/private/stop-order/create
    // /v2/private/order/create
    public enum OrderType {
        Market,
        Limit,
        Conditional
    }

    public Order(OrderType type, Side side, double price, int qty) {
        // TODO create initial http request here
        this.type = type;
        this.side = side;
        this.price = price;
        this.qty = qty;
        this.filled = false;
        this.id = null; // TODO id generation
    }

    public Side side() {
        return this.side;
    }

    public double price() {
        return this.price;
    }

    public int qty() {
        return this.qty;
    }

    public String id() {
        return this.id;
    }

    public boolean isFilled() {
        return this.filled;
    }
    
    public OrderType type() {
        return this.type;
    }

    public void amendPrice(double price) {
        if (!this.isFilled()) {
            this.price = price;
        }
        // make replace request here /v2/private/order/replace
    }
}
