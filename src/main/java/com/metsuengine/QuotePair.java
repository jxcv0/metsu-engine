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
            this.ask = Optional.of(order);
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

    public void update(List<Order> orders) {
        Optional<Order> optionalBid = orders.stream().filter(o -> o.side().equals(Side.Buy)).findAny();
        if (optionalBid.isPresent()) {
            switch (optionalBid.get().orderStatus()) {
                case New:
                    setBid(bid);
                    break;
                
                case Filled:
                    setBid(Optional.empty());
                    break;

                case Created:
                    setBid(bid);
                    break;

                case Cancelled:
                    setBid(Optional.empty());
                    break;

                case PartiallyFilled:
                    setBid(bid);
                    break;

                case PendingCancel:
                    setBid(Optional.empty());
                    break;

                case Rejected:
                    setBid(Optional.empty());
                    break;

                default:
                    break;
            }
        }

        Optional<Order> optionalAsk = orders.stream().filter(o -> o.side().equals(Side.Sell)).findAny();
        if (optionalAsk.isPresent()) {
            switch (optionalAsk.get().orderStatus()) {
                case New:
                    setAsk(ask);
                    break;
                
                case Filled:
                    setAsk(Optional.empty());
                    break;

                case Created:
                    setAsk(ask);
                    break;

                case Cancelled:
                    setAsk(Optional.empty());
                    break;

                case PartiallyFilled:
                    setBid(bid);
                    break;

                case PendingCancel:
                    setAsk(Optional.empty());
                    break;

                case Rejected:
                    setAsk(Optional.empty());
                    break;

                default:
                    break;
            }
        }
    }
}
