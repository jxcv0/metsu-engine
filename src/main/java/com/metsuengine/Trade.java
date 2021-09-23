package com.metsuengine;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Trade implements Serializable{
    
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

    public ZonedDateTime time() {
        return this.time;
    }

    public String side() {
    	return this.side;
    }

    public double price() {
    	return this.price;
    }

    public double size() {
    	return this.size;
    }
}
