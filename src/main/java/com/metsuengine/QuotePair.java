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

    public Optional<Order> bid() {
        return bid;
    }

    public Optional<Order> ask() {
        return ask;
    }

    public void update(List<Order> orders) {
        this.bid = orders.stream().filter(o -> o.side().equals(Side.Buy)).findAny();
        this.ask = orders.stream().filter(o -> o.side().equals(Side.Sell)).findAny();
    }

    // Optional<Order> optionalBid = orders.stream().filter(o -> o.side().equals(Side.Buy)).findAny();
}
