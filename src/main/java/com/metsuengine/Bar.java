package com.metsuengine;

import java.time.ZonedDateTime;

public class Bar {
    
    private ZonedDateTime time = null;
    private double deltaRatio = 0;
    private double price = 0;

    public Bar(ZonedDateTime time, double deltaRatio, double price){
        this.time = time;
        this.deltaRatio = deltaRatio;
        this.price = price;
    }

    public ZonedDateTime getTime() {
        return this.time;
    }

    public double getDeltaRatio() {
        return this.deltaRatio;
    }

    public double getPrice() {
        return this.price;
    }
}
