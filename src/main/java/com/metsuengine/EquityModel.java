package com.metsuengine;

import java.time.ZonedDateTime;
import java.util.HashMap;

public class EquityModel {

    private double equity;
    private HashMap<ZonedDateTime, Double> equityCurve;

    public EquityModel(double equity) {
        this.equity = equity;
        this.equityCurve = new HashMap<ZonedDateTime, Double>();
    }

    public void add(ZonedDateTime time, double pnlMultiplier) {
        this.equityCurve.put(time, equity*pnlMultiplier);
    }

    public double equity() {
        return equity;
    }

    public void printValue() {
        System.out.println(this.equity());
    }

    public void mult(double pnlMultiplier) {
        this.equity *= pnlMultiplier;
    }

    public HashMap<ZonedDateTime, Double> map() {
        return equityCurve;
    }
}
