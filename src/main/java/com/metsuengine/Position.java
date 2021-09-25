package com.metsuengine;

public class Position {

    private final int id;
    private double entry;
    private double exit;
    private Side side;
    private double stopLoss;
    private double takeProfit;
    private boolean filled;
    private boolean closed;

    public Position(int id, Side side, double entry, double stopLoss, double takeProfit) {
        // TODO make httpRequest here?
        this.id = id;
        this.side = side;
        this.entry = entry;
        this.exit = entry;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        this.filled = false;
        this.closed = false;
    }

    public void close(double exit) {
        this.exit = exit;
        this.closed = true;
    }

    public double pnl() {
        if (this.side == Side.LONG ) {
            return this.exit/this.entry;
        } else {
            return this.entry/this.exit;
        }
    }

    public int getId() {
        return this.id;
    }

    public void fill() {
        this.filled = true;
    }

    public double getEntry() {
    	return this.entry;
    }
    public void setEntry(double entry) {
    	this.entry = entry;
    }

    public double getExit() {
    	return this.exit;
    }

    public Side getSide() {
    	return this.side;
    }
    public void setSide(Side side) {
    	this.side = side;
    }

    public double getStopLoss() {
    	return this.stopLoss;
    }
    public void setStopLoss(double stopLoss) {
    	this.stopLoss = stopLoss;
    }

    public double getTakeProfit() {
    	return this.takeProfit;
    }
    public void setTakeProfit(double takeProfit) {
    	this.takeProfit = takeProfit;
    }

    public boolean isFilled() {
        return this.filled;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public enum Side {
        LONG,
        SHORT
    }
}
