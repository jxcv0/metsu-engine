package com.metsuengine;

public class Enums {

    public enum Side {
        Buy,
        Sell,
        None
    }

    public enum OrderType {
        Limit,
        Market
    }

    public enum OrderStatus {
        Created,                // order has been accepted by the system but not yet put through the matching engine
        Rejected,
        New,                    // order has been placed successfully
        PartiallyFilled,
        Filled,
        Cancelled,
        PendingCancel,          // matching engine has received the cancelation request but it may not be canceled successfully
    }
    
    public enum TimeInForce {
        GoodTillCancel,
        ImmediateOrCancel,
        FillOrKill,
        PostOnly
    }
}
