package com.metsuengine;

import java.time.ZonedDateTime;
import java.util.HashMap;

public class BackTest {

    private double previousTradedPrice;
    private double previousValue;
    private MovingAverage movingAverage;
    private HashMap<ZonedDateTime, Double> crossUp;
    private HashMap<ZonedDateTime, Double> crossDown;

    public BackTest(final MovingAverage movingAverage) {
        this.movingAverage = movingAverage;
        this.crossUp = new HashMap<ZonedDateTime, Double>();
        this.crossDown = new HashMap<ZonedDateTime, Double>();
    }

    public boolean checkCrossedUp(Trade trade) {
        if (trade.price() > movingAverage.value() && previousTradedPrice < previousValue) {
            previousTradedPrice = trade.price();
            previousValue = movingAverage.value();
            crossUp.put(trade.time(), trade.price());
            return true;
        } else {
            previousTradedPrice = trade.price();
            previousValue = movingAverage.value();
            return false;
        }
    }

    public boolean checkCrossedDown(Trade trade) {
        if (trade.price() < movingAverage.value() && previousTradedPrice > previousValue) {
            previousTradedPrice = trade.price();
            previousValue = movingAverage.value();
            crossDown.put(trade.time(), trade.price());
            return true;
        } else {
            previousTradedPrice = trade.price();
            previousValue = movingAverage.value();
            return false;
        }
    }

    public HashMap<ZonedDateTime, Double> crossUp() {
        return crossUp;
    }

    public HashMap<ZonedDateTime, Double> crossDown() {
        return crossDown;
    }
}