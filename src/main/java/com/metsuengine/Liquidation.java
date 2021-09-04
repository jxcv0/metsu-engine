package com.metsuengine;

import java.time.ZonedDateTime;

public class Liquidation {

    private final double size;
    private final String side;
    private final ZonedDateTime time;
    private final String symbol;
    private final double price;

    public Liquidation(double size, String side, ZonedDateTime time, String symbol, double price) {
        this.size = size;
        this.side = side;
        this.time = time;
        this.symbol = symbol;
        this.price = price;
    }

    public double getSize() {
        return this.size;
    }
    
    public String getSide() {
        return this.side;
    }

    public ZonedDateTime getTime() {
        return this.time;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public double getPrice() {
        return this.price;
    }
}
