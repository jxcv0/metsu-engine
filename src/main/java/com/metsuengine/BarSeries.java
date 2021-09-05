package com.metsuengine;

import java.util.ArrayList;
import java.util.List;

public class BarSeries {

    private String name = null;
    private List<Bar> series = new ArrayList<Bar>();

    public BarSeries() {
        this.name = "unnamed";
        this.series = new ArrayList<>();
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

    public List<Bar> getSeries() {
        return this.series;
    }

    public Bar getBar(int index) {
        return this.series.get(index);
    }

    public void addBar(Bar bar) {
        this.series.add(bar);
    }

    public int getBarCount() {
        return this.series.size();
    }
}
