package com.metsuengine;

import com.metsuengine.Order.Side;

public class Position {

    private final Order entry;
    private final Order exit;
    private final double stopLoss;

    public Position(Side side, int qty, double initialEntry, double exit, double stopLoss) {
        switch (side) {
            case LONG:
                this.entry = new Order(side, initialEntry, qty);
                this.exit = new Order(Side.SHORT, exit, qty);
                this.stopLoss = stopLoss;
                break;
        
            default:
                this.entry = new Order(side, initialEntry, qty);
                this.exit = new Order(Side.SHORT, exit, qty);
                this.stopLoss = stopLoss;
                break;
        }
    } 
}
