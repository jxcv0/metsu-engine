package com.metsuengine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FrameSeries implements Serializable {

    private String name = null;
    private List<Frame> series = new ArrayList<Frame>();

    public FrameSeries() {
        this.name = "unnamed";
        this.series = new ArrayList<>();
    }

    public FrameSeries(String name, List<Frame> series) {
        this.name = name;
        this.series = series;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Frame> getSeries() {
        return this.series;
    }

    public Frame getFrame(int index) {
        return this.series.get(index);
    }

    public void addFrame(Frame frame) {
        this.series.add(frame);
    }

    public int getFrameCount() {
        return this.series.size();
    }

    public List<Double> getSeriesOrderBookRatio() {
        List<Double> depthRatio = new ArrayList<Double>();
        for (Frame frame : this.series) {
            depthRatio.add(frame.getOrderBook().getDeltaRatio());
        }

        return depthRatio;
    }

    public List<Double> getSeriesOrderBookDepth(String side) {
        List<Double> depth = new ArrayList<Double>();
        for (Frame frame : this.series) {
            depth.add(frame.getOrderBook().getDepth(side));
        }

        return depth;
    } 

    public List<Double> getSeriesBestBid() {
        List<Double> bestBids = new ArrayList<Double>();
        for (Frame frame : this.series) {
            bestBids.add(frame.getOrderBook().getBestBid());
        }

        return bestBids;
    }

    public List<Double> getSeriesBestAsk() {
        List<Double> bestAsks = new ArrayList<Double>();
        for (Frame frame : this.series) {
            bestAsks.add(frame.getOrderBook().getBestAsk());
        }

        return bestAsks;
    }

    // TODO moving series for trading
}
