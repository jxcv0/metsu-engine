package com.metsuengine;

import java.util.List;

public class Strategy {

    private List<Double> highVolumeNodes;
    private List<Double> lowVolumeNodes;
    private Trade lastTrade;
    private double vwap;

    public Strategy(List<Double> highVolumeNodes, List<Double> lowVolumeNodes) {
        this.highVolumeNodes = highVolumeNodes;
        this.lowVolumeNodes = lowVolumeNodes;
    }

    public void update(Trade trade) {
        this.lastTrade = trade;
    }

    public void setVwap(double vwap) {
        this.vwap = vwap;
    }
}
