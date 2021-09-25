package com.metsuengine;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Strategy {

    private List<Double> highVolumeNodes;
    private List<Double> lowVolumeNodes;
    private List<OrderSet> orders;
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

    public void init() {
        for (double highVolumeNode : highVolumeNodes) {
            if (lastTrade.price() > highVolumeNode) {
                // create buy orders
            } else {
                // create sell orders
            }
        }
    }

    private void compare(double num) {
        // compare to vwap
    }
}
