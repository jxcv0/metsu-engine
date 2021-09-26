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
        this.state = State.Passive;
    }

    public void evaluate(double price) {
        if (this.state.equals(State.Passive) && price == this.entryPrice) {

            this.state = State.Filled;
            System.out.println("Filled at " + price);

        } else if (this.state.equals(State.Filled)) {
            if (price == this.stopLoss) {
                this.exitPrice = price;
                this.state = State.Closed;
                System.out.println("Closed at SL: " + price);

            } else if (price == this.takeProfit) {
                this.exitPrice = price;
                this.state = State.Closed;
                System.out.println("Closed at TP: " + price);
                
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
}
