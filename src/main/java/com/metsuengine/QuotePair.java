package com.metsuengine;

import java.util.Optional;

import com.metsuengine.Enums.State;

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
        switch (order.orderStatus()) {
            case New:
                bid = Optional.of(order);
                break;
            
            case Filled:
                bid = Optional.empty();
                break;

            case Created:
                bid = Optional.of(order);
                break;

            case Cancelled:
                bid = Optional.empty();
                break;

            case PartiallyFilled:
                bid = Optional.of(order);
                break;

            case PendingCancel:
                bid = Optional.empty();
                break;

            case Rejected:
                bid = Optional.empty();
                break;

            default:
                break;
        }
    }

    public void setAsk(Order order) {
        switch (order.orderStatus()) {
            case New:
                ask = Optional.of(order);
                break;
            
            case Filled:
                ask = Optional.empty();
                break;

            case Created:
                ask = Optional.of(order);
                break;

            case Cancelled:
                ask = Optional.empty();
                break;

            case PartiallyFilled:
                ask = Optional.of(order);
                break;

            case PendingCancel:
                ask = Optional.empty();
                break;

            case Rejected:
                ask = Optional.empty();
                break;

            default:
                break;
        }
    }

    public Optional<Order> bid() {
        return bid;
    }

    public Optional<Order> ask() {
        return ask;
    }

    public void update(Order order) {
        switch (order.side()) {
            case Buy:
                setBid(order);
                break;
            
            case Sell:
                setAsk(order);
                break;
        
            default:
                break;
        }
    }

    public State state() {
        if (bid.isPresent() && ask.isPresent()) {
            return State.HasBoth;
        } else if (bid.isPresent() && ask.isEmpty()) {
            return State.HasBid;
        } else if (bid.isEmpty() && ask.isPresent()) {
            return State.HasAsk;
        } else {
            return State.HasNone;
        }
    }
}
