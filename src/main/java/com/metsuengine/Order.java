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
    private String orderId;

    public Order (String symbol, Side side, OrderType orderType, TimeInForce timeInForce) {
        this.symbol = symbol;
        this.side = side;
        this.orderType = orderType;
        this.timeInForce = timeInForce;
    }
    
    public Order (String symbol, Side side, OrderType orderType, double price, double qty, TimeInForce timeInForce, OrderStatus orderStatus) {
        this.symbol = symbol;
        this.side = side;
        this.orderType = orderType;
        this.price = price;
        this.qty = qty;
        this.timeInForce = timeInForce;
        this.orderStatus = orderStatus;
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

    public void updatePrice(double price) {
        this.price = price;
    }

    public double qty() {
        return qty;
    }

    public void updateQty(double qty) {
        this.qty = qty;
    }

    public TimeInForce timeInForce() {
        return timeInForce;
    }

    public OrderStatus orderStatus() {
        return orderStatus;
    }

    public String orderId() {
        return orderId;
    }

    public void setId(String orderId) {
        this.orderId = orderId;
    }

    public void setStatus(OrderStatus status) {
        this.orderStatus = status;
    }

    public boolean isEquivalentTo(Order order) {
        if (order.side() == this.side && order.price() == this.price && order.qty() == this.qty) {
            return true;
        } else {
            return false;
        }
    }   
}