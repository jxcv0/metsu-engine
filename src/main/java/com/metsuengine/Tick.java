package com.metsuengine;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Tick implements Serializable {
    
    private ZonedDateTime time;
    private String side;
    private double price;
    private double size;
    
    public Tick(ZonedDateTime time, String side, double price, double size) {
        this.time = time.withFixedOffsetZone();
        this.side = side;
        this.price = price;
        this.size = size;
    }

    public Tick(String time, String side, double price, double size) {
        this.time = ZonedDateTime.parse(time).withFixedOffsetZone();
        this.side = side;
        this.price = price;
        this.size = size;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public void setTime(String time) {
        this.time = ZonedDateTime.parse(time);
    }

    public ZonedDateTime time() {
        return this.time;
    }

    public void setSide(String side) {
        this.side = side;
    } 

    public String side() {
    	return this.side;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double price() {
    	return this.price;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double size() {
    	return this.size;
    }

    public double signedValue() {
        if (side.equals("Sell")) {
            return -size;
        } else {
            return size;
        }
    }
}
