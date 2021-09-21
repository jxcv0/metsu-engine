package com.metsuengine;

public class Position {

    private final int id;
    private double entry;
    private double exit;
    private Side side;
    private double stopLoss;
    private double takeProfit;
    private boolean filled;

    public enum Side {
        LONG,
        SHORT
    }

    public Position(int id, Side side, double entry, double stopLoss, double takeProfit) {
        // TODO make httpRequest here
        this.id = id;
        this.side = side;
        this.entry = entry;
        this.exit = entry;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
    }

    public void close(double exitPrice) {
        this.exit = exitPrice;
    }

    public double pnl() {
        if (this.side == Side.LONG ) {
            return this.exit/this.entry;
        } else {
            return this.entry/this.exit;
        }
    }

    public boolean isFilled() {
        return this.filled;
    }

    public void fill() {
        this.filled = true;
    }
}
