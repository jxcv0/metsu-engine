package com.metsuengine;

import com.metsuengine.Enums.OrderStatus;
import com.metsuengine.Enums.OrderType;
import com.metsuengine.Enums.Side;
import com.metsuengine.Enums.TimeInForce;

public class Order {

    private String symbol;
    private Side side;
    private OrderType orderType;
    private double price;
    private double qty;
    private TimeInForce timeInForce;
    private OrderStatus orderStatus;
    private String orderLinkId;

    public Order (String symbol, Side side, OrderType orderType, double price, double qty, TimeInForce timeInForce, String orderLinkId) {
        this.symbol = symbol;
        this.side = side;
        this.orderType = orderType;
        this.price = price;
        this.qty = qty;
        this.timeInForce = timeInForce;
        this.orderLinkId = orderLinkId;
    }
    
    public Order (String symbol, Side side, OrderType orderType, double price, double qty, TimeInForce timeInForce, OrderStatus orderStatus, String orderLinkId) {
        this.symbol = symbol;
        this.side = side;
        this.orderType = orderType;
        this.price = price;
        this.qty = qty;
        this.timeInForce = timeInForce;
        this.orderStatus = orderStatus;
        this.orderLinkId = orderLinkId;
    }
    
    public String symbol() {
        return symbol;
    }

    public Side side() {
        return side;
    }

    public OrderType orderType() {
        return orderType;
    }

    public double price() {
        return price;
    }

    public double qty() {
        return qty;
    }

    public TimeInForce timeInForce() {
        return timeInForce;
    }

    public OrderStatus orderStatus() {
        return orderStatus;
    }

    public String orderLinkId() {
        return orderLinkId;
    }

    public void setStatus(OrderStatus status) {
        this.orderStatus = status;
    }
}
