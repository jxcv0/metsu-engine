package com.metsuengine;

import java.util.ArrayList;
import java.util.List;

public class PositionManager {

    private List<Double> targets;
    private double lastTradedPrice;
    
    public PositionManager() {
        this.targets = new ArrayList<Double>();
    }

    public void addTargetLevel(double target) {
        targets.add(target);
    }

    /* TODO
    ** if within 1stddev, chase 1stddev (fill order, update net pos)
    ** if within 2stddev, chase 2stdeev
    ** if > 2stdev (fill order, update net pos)
    ** if net position !=, chase mean with -1 * net position   
    */
}
