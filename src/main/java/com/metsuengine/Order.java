package com.metsuengine;

public class Order {

    private Side side;
    private double entryPrice;
    private double exitPrice;
    private double takeProfit;
    private double stopLoss;
    private int qty;
    private State state;
    private String id;
    
    public enum Side {
        Buy,
        Sell
    }

    public enum State {
        Passive,
        Filled,
        Closed
    }

    public Order(Side side, int qty, double price, double takeProfit, double stopLoss) {
        // TODO create http request here
        this.side = side;
        this.entryPrice = price;
        this.qty = qty;
        this.takeProfit = takeProfit;
        this.stopLoss = stopLoss;
        this.state = State.Passive;
    }

    public void evaluate(double price) {
        if (this.side.equals(Side.Buy)) {
            switch (this.state) {
                case Passive:
                    if (price > this.entryPrice) {
                        this.state = State.Filled;
                        // System.out.println("Filled " + this.side + " at " + this.entryPrice);
                    }
                    break;
                
                case Filled:
                    if (price <= this.stopLoss) {
                        this.exitPrice = price;
                        this.state = State.Closed;
                        // System.out.println("Closed at SL: " + price + " " + this.pnlMultiplier());
                    } else if (price == this.takeProfit) {
                        this.exitPrice = price;
                        this.state = State.Closed;
                        // System.out.println("Closed at TP: " + price + " " + this.pnlMultiplier());
                    }
                default:
                    break;
            }
        } else {
            switch (this.state) {
                case Passive:
                    if (price > this.entryPrice) {
                        this.state = State.Filled;
                        // System.out.println("Filled " + this.side + " at " + this.entryPrice);
                    }
                    break;
                
                case Filled:
                    if (price >= this.stopLoss) {
                        this.exitPrice = price;
                        this.state = State.Closed;
                        // System.out.println("Closed at SL: " + price + " " + this.pnlMultiplier());
                    } else if (price <= this.takeProfit) {
                        this.exitPrice = price;
                        this.state = State.Closed;
                        // System.out.println("Closed at TP: " + price + " " + this.pnlMultiplier());
                    }
            
                default:
                    break;
            }
        }
    }

    public Side side() {
        return this.side;
    }

    public int qty() {
        return this.qty;
    }

    public double entryPrice() {
        return this.entryPrice;
    }

    public double takeProfit() {
        return this.takeProfit;
    }

    public double stopLoss() {
        return this.stopLoss;
    }

    public String id() {
        return this.id;
    }

    public State state() {
        return this.state;
    }

    public double pnlMultiplier() {
        if (this.side == Side.Buy) {
            return (exitPrice/entryPrice) * .995;
        } else {
            return (entryPrice/exitPrice) * .995;
        }
    }
}
