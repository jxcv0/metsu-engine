package com.metsuengine;

import java.time.ZonedDateTime;

public class Bar {
    
    private ZonedDateTime time = null;
    private double price = 0;
    private double bidDepth = 0;
    private double askDepth = 0;
    private double deltaRatio = 0;

    public Bar(ZonedDateTime time, double price, double bidDepth, double askDepth, double deltaRatio){
        this.time = time;
        this.price = price;
        this.bidDepth = bidDepth;
        this.askDepth = askDepth;
        this.deltaRatio = deltaRatio;

    }

    public ZonedDateTime getTime() {
        return this.time;
    }

    public double getPrice() {
        return this.price;
    }

    public double getAskDepth() {
        return this.askDepth;
    }

    public double getBidDepth() {
        return this.bidDepth;
    }
    
    public double getDeltaRatio() {
        return this.deltaRatio;
    }
}
