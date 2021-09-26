package com.metsuengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.metsuengine.Order.Side;

public class Strategy {

    private List<Double> highVolumeNodes;
    private List<Double> lowVolumeNodes;
    private List<Order> orders;
    private Trade lastTrade;
    private boolean initialized;

    public Strategy(List<Double> highVolumeNodes, List<Double> lowVolumeNodes) {
        this.highVolumeNodes = highVolumeNodes;
        this.lowVolumeNodes = lowVolumeNodes;
        this.orders = new ArrayList<Order>();
        this.initialized = false;
    }

    public void update(Trade lastTrade) {
        this.lastTrade = lastTrade;

        if (!this.initialized) {
            this.init();
            this.initialized = true;
        }
            
        for (Order order : orders) {
            order.evaluate(this.lastTrade.price());
        }
    }

    public void init() {
        for (double highVolumeNode : highVolumeNodes) {
            if (lastTrade.price() > highVolumeNode) {
                orders.add(new Order(
                    Side.Buy,
                    1,
                    highVolumeNode,
                    lowVolumeNodeAbove(highVolumeNode),
                    lowVolumeNodeBelow(highVolumeNode)));
            } else {
                orders.add(new Order(
                    Side.Sell,
                    1,
                    highVolumeNode,
                    lowVolumeNodeBelow(highVolumeNode),
                    lowVolumeNodeAbove(highVolumeNode)));
            }
        }
        this.listPositions();
    }

    private double lowVolumeNodeAbove(double highVolumeNode) {

        List<Double> above = new ArrayList<Double>();
        for (double lowVolumeNode : this.lowVolumeNodes) {
            if (lowVolumeNode > highVolumeNode) {
                above.add(lowVolumeNode);
            }
        }

        if (above.size() < 1) {
            return round(highVolumeNode + 100);
        } else {
            return Collections.min(above);
        }
    }

    private double lowVolumeNodeBelow(double highVolumeNode) {
        List<Double> below = new ArrayList<Double>();
        for (double lowVolumeNode : this.lowVolumeNodes) {
            if (lowVolumeNode < highVolumeNode) {
                below.add(lowVolumeNode);
            }
        }

        if (below.size() < 1) {
            return round(highVolumeNode - 100);
        } else {
            return Collections.max(below);
        }
    }

    public void listPositions() {
        for (Order order : orders) {
            System.out.println(
                order.state() + " " + 
                order.side() + " " + 
                order.entryPrice() + " " + 
                order.stopLoss() + " " +
                order.takeProfit());            
        }
    }

    private double round(double num) {
        return Math.round(num * 2) / 2.0;
    }
}
