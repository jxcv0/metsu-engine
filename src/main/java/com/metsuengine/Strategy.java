package com.metsuengine;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Strategy {

    private static final AtomicInteger count = new AtomicInteger(0); 
    private List<Double> highVolumeNodes;
    private List<Double> lowVolumeNodes;
    private Trade lastTrade;
    private VWAP vwap;

    public Strategy(List<Double> highVolumeNodes, List<Double> lowVolumeNodes, VWAP vwap) {
        this.highVolumeNodes = highVolumeNodes;
        this.lowVolumeNodes = lowVolumeNodes;
        this.vwap = vwap;
    }

    public void update(Trade lastTrade) {
        this.lastTrade = lastTrade;
    }

    private double round(double num) {
        return Math.round(num * 2) / 2.0;
    }
}
