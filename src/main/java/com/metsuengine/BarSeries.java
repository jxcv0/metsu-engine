package com.metsuengine;

import java.util.ArrayList;
import java.util.List;

public class BarSeries {

    private String name = null;
    private List<Bar> series = new ArrayList<Bar>();

    public BarSeries() {
        this.series = null;
    }

    public BarSeries(String name, List<Bar> series) {
        this.name = name;
        this.series = series;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Bar> getBarSeries() {
        return this.series;
    }

    public void addBar(Bar bar) {
        this.series.add(bar);
    }
}
