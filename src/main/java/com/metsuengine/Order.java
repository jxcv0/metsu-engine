package com.metsuengine;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Order {

    private String id;
    private ZonedDateTime time;
    private Side side;
    private double limit;
    private int qty;

    public enum Side {
        Buy,
        Sell
    }

    public Order(String id, Side side, double limit, int qty) {
        this.id = id;
        this.time = ZonedDateTime.now(ZoneOffset.UTC);
        this.side = side;
        this.limit = limit;
        this.qty = qty;
    }

    public ZonedDateTime time() {
        return time;
    }

    public String id() {
        return id;
    }

    public Side side() {
        return side;
    }

    public double limit() {
        return limit;
    }

    public int qty() {
        return qty;
    }

    public void updateLimit(double limit) {
        this.limit = limit;
    }
}
