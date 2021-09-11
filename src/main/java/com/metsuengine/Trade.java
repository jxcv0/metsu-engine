package com.metsuengine;

import java.time.ZonedDateTime;

public class Trade {
    
    private final ZonedDateTime time;
    private final String side;
    private final double price;
    private final double size;
    
    public Trade(ZonedDateTime time, String side, double price, double size) {
        this.time = time;
        this.side = side;
        this.price = price;
        this.size = size;
    }

    public Trade(String time, String side, double price, double size) {
        this.time = ZonedDateTime.parse(time);
        this.side = side;
        this.price = price;
        this.size = size;
    }

    public ZonedDateTime getTime() {
        return this.time;
    }

    public String getSide() {
    	return this.side;
    }

    public double getPrice() {
    	return this.price;
    }

    public double getSize() {
    	return this.size;
    }
}
