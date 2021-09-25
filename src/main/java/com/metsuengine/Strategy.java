package com.metsuengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.metsuengine.Order.Side;

public class Strategy {

    private List<Double> highVolumeNodes;
    private List<Double> lowVolumeNodes;
    private List<OrderSet> orders;
    private Trade lastTrade;
    private VWAP vwap;
    private boolean initialized;

    public Strategy(List<Double> highVolumeNodes, List<Double> lowVolumeNodes, VWAP vwap) {
        this.highVolumeNodes = highVolumeNodes;
        this.lowVolumeNodes = lowVolumeNodes;
        this.orders = new ArrayList<OrderSet>();
        this.vwap = vwap;
        this.initialized = false;
    }

    public void update(Trade lastTrade) {
        this.lastTrade = lastTrade;
        if (!this.initialized) {
            this.init();
            this.initialized = true;
        }
    }

    public void init() {
        for (double highVolumeNode : highVolumeNodes) {
            if (lastTrade.price() > highVolumeNode) {
                orders.add(new OrderSet(
                    Side.Buy,
                    1,
                    highVolumeNode,
                    lowVolumeNodeAbove(highVolumeNode),
                    lowVolumeNodeBelow(highVolumeNode)));
            } else {
                orders.add(new OrderSet(
                    Side.Sell,
                    1,
                    highVolumeNode,
                    lowVolumeNodeBelow(highVolumeNode),
                    lowVolumeNodeAbove(highVolumeNode)));
            }
        }
        this.listPositions();
    }

    private void compare(double num) {
        // compare to vwap
    }

    private double lowVolumeNodeAbove(double highVolumeNode) {
        List<Double> above = new ArrayList<Double>();
        for (double lowVolumeNode : lowVolumeNodes) {
            if (lowVolumeNode > highVolumeNode) {
                above.add(lowVolumeNode);
            }
        }

        if (above.size() > 1) {
            return highVolumeNode * 1.01;
        } else {
            return Collections.min(above);
        }
    }

    private double lowVolumeNodeBelow(double highVolumeNode) {
        List<Double> below = new ArrayList<Double>();
        for (double lowVolumeNode : lowVolumeNodes) {
            if (lowVolumeNode < highVolumeNode) {
                below.add(lowVolumeNode);
            }
        }

        if (below.size() < 1) {
            return highVolumeNode * 0.99;
        } else {
            return Collections.max(below);
        }
    }

    public boolean isAbove(double num1, double num2) {
        return num1 > num2;
    }

    public void listPositions() {
        for (OrderSet orderSet : orders) {
            System.out.println(orderSet.entry().side().toString() + " " + orderSet.entry().price());           
        }
    }
}
