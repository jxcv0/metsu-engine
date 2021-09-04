package com.metsuengine;

import java.time.ZonedDateTime;

public class Trade {
    
    private double entryprice;
    private double exitprice;
    private ZonedDateTime entryTime;
    private ZonedDateTime exitTime;

    public Trade(ZonedDateTime entryTime, double entryprice) {
        this.entryTime = entryTime;
        this.entryprice = entryprice;
    }

    public double getEntryprice() {
        return this.entryprice;
    }
    public void setEntryprice(double entryprice) {
        this.entryprice = entryprice;
    }

    public double getExitprice() {
    	return this.exitprice;
    }
    public void setExitprice(double exitprice) {
    	this.exitprice = exitprice;
    }

    public ZonedDateTime getEntryTime() {
    	return this.entryTime;
    }
    public void setEntryTime(ZonedDateTime entryTime) {
    	this.entryTime = entryTime;
    }

    public ZonedDateTime getExitTime() {
    	return this.exitTime;
    }
    public void setExitTime(ZonedDateTime exitTime) {
    	this.exitTime = exitTime;
    }
}
