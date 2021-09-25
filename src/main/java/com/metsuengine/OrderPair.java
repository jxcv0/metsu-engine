package com.metsuengine;

import com.metsuengine.Order.OrderType;
import com.metsuengine.Order.Side;

public class OrderPair {

    private final Order entry;
    private final Order takeProfit;
    private final Order stopOrder;

    public OrderPair(Side side, int qty, double initialEntry, double takeProfit, double stopLoss) {
        switch (side) {
            case Buy:
                this.entry = new Order(OrderType.Limit, side, initialEntry, qty);
                this.takeProfit = new Order(OrderType.Limit, Side.Sell, takeProfit, qty);
                this.stopOrder = new Order(OrderType.Conditional, Side.Sell, stopLoss, qty);
                break;
        
            default:
                this.entry = new Order(OrderType.Limit, side, initialEntry, qty);
                this.takeProfit = new Order(OrderType.Limit, Side.Buy, takeProfit, qty);
                this.stopOrder = new Order(OrderType.Conditional, Side.Buy, stopLoss, qty);
                break;
        }
    }

    public Order entry() {
        return this.entry;
    }
    
    public Order takeProfit() {
        return this.takeProfit;
    }
    
    public Order stopOrder() {
        return this.stopOrder;
    }
}
