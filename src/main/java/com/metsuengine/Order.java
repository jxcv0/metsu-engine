package com.metsuengine;

public class Order {

    private double entry;
    private double exit;
    private Side side;
    
    public enum Side {
        LONG,
        SHORT
    }

    public Order(Side side) {
        this.side = side;
    }

    public void close(double exit) {
        this.exit = exit;
    }

    public double pnl() {
        if (this.side == Side.LONG ) {
            return this.exit/this.entry;
        } else {
            return this.entry/this.exit;
        }
    }

    public double entry() {
    	return this.entry;
    }

    public double exit() {
    	return this.exit;
    }
}
