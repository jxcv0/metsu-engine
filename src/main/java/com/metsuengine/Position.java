package com.metsuengine;

import com.metsuengine.Enums.Side;

public class Position {

    private String symbol;
    private Side side;
    private double size;
    private double entryPrice;

    public Position(String symbol, Side side, double size, double entryPrice) {
        this.symbol = symbol;
        this.side = side;
        this.size = size;
        this.entryPrice = entryPrice;
    }

    public String symbol() {
        return symbol;
    }

    public Side side() {
        return side;
    }

    public double size() {
        return size;
    }

    public double entryPrice() {
        return entryPrice;
    }
 }
