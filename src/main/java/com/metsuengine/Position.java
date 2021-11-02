package com.metsuengine;

import com.metsuengine.Enums.Side;

public class Position {

    private String symbol;
    private Side side;
    private double size;
    private double entryPrice;
    
    public Position() {
        this.side = Side.None;
        this.size = 0;
        this.entryPrice = 0;
    }

    public String symbol() {
        return symbol;
    }

    public Side side() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public double size() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double signedValue() {
        switch (side) {
            case Buy:
                return size;

            case Sell:
                return -size;
        
            default:
                return 0;
        }
    }

    public double entryPrice() {
        return entryPrice;
    }

    public void setEntryPrice(double price) {
        this.entryPrice = price;
    }
 }
