package com.metsuengine;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import com.metsuengine.Enums.Side;

public class QuotePair {
    private Optional<Order> bid;
    private Optional<Order> ask;

    public QuotePair() {
        this.bid = Optional.empty();
        this.ask = Optional.empty();
    }

    public QuotePair(Order bid, Order ask) {
        this.bid = Optional.of(bid);
        this.ask = Optional.of(ask);
    }

    public void setBid(Order order) {
        if (order.side() == Side.Buy) {
            this.bid = Optional.of(order);
        } else {
            throw new InvalidParameterException("New bid quote must be a buy order");
        }
    }

    public void setAsk(Order order) {
        if (order.side() == Side.Sell) {
            this.bid = Optional.of(order);
        } else {
            throw new InvalidParameterException("New ask quote must be a sell order");
        }
    }

    public void setBid(Optional<Order> order) {
        this.bid = order;
    }

    public void setAsk(Optional<Order> order) {
        this.ask = order;
    }

    public Optional<Order> bid() {
        return bid;
    }

    public Optional<Order> ask() {
        return ask;
    }

    // only set optional to empty if filled
    public void update(List<Order> orders) {
        orders.stream().filter(o -> o.side().equals(Side.Buy)).findAny()
            .ifPresent(o -> setBid(o));
        
        orders.stream().filter(o -> o.side().equals(Side.Sell)).findAny()
            .ifPresent(o -> setAsk(o));
    }
}
