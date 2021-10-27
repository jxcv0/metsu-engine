package com.metsuengine;

import com.metsuengine.Enums.OrderStatus;
import com.metsuengine.Enums.OrderType;
import com.metsuengine.Enums.TimeInForce;

public class Order {

    private final String symbol;
    private final String side;
    private final OrderType orderType;
    private double price;
    private double qty;
    private final TimeInForce timeInForce;
    private OrderStatus orderStatus;
    private final String orderLinkId;
    
    public Order(String symbol, String side, OrderType orderType, double price, double qty, TimeInForce timeInForce,
            OrderStatus orderStatus, String orderLinkId) {
        this.symbol = symbol;
        this.side = side;
        this.orderType = orderType;
        this.price = price;
        this.qty = qty;
        this.timeInForce = timeInForce;
        this.orderStatus = orderStatus;
        this.orderLinkId = orderLinkId;
    }

    // "symbol": "BTCUSD",
    // "side": "Buy",
    // "order_type": "Limit",
    // "price": "11756.5",
    // "qty": 1,
    // "time_in_force": "PostOnly",
    // "order_status": "Filled",
    // "order_link_id": "",
    
}
