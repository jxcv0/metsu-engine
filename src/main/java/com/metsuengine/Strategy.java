package com.metsuengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.metsuengine.Position.Side;

public class Strategy {

    private static final AtomicInteger count = new AtomicInteger(0); 
    private List<Position> positions;
    private List<Double> highVolumeNodes;
    private List<Double> lowVolumeNodes;
    private Trade lastTrade;
    private double vwap;

    public Strategy(List<Double> highVolumeNodes, List<Double> lowVolumeNodes) {
        this.highVolumeNodes = highVolumeNodes;
        this.lowVolumeNodes = lowVolumeNodes;
    }

    public void update(Trade trade, double vwap) {
        this.lastTrade = trade;
        //initialize positions (if lastTrade is not null)
        // check if a position is fillable
        // if fillable, fill check if closable
        // if closeable calculate returns and save pnl, check if reversable
        // if reversable make new position
        System.out.println(lastTrade.price());

    }

    public void setVWAP(double vwap) {
        this.vwap = vwap;
    }

    public void initializePositions() { 
        for (double hvn : highVolumeNodes) {
            if (lastTrade.price() > hvn) {
                positions.add(new Position(
                    count.incrementAndGet(),
                    Side.SHORT, 
                    hvn, 
                    findlvnAbove(hvn),
                    checkTakeProfit(findlvnBelow(hvn), Side.SHORT)));
            } else {
                positions.add(new Position(
                    count.incrementAndGet(),
                    Side.LONG, 
                    hvn, 
                    findlvnAbove(hvn),
                    checkTakeProfit(findlvnBelow(hvn), Side.LONG)));
            }
        }
        printPositions();
    }

    private double findlvnAbove(double hvn){
        List<Double> lowVolumeNodesAbove = new ArrayList<Double>();
        for (double lvn : lowVolumeNodes) {
            if (lvn > hvn) {
                lowVolumeNodesAbove.add(lvn);
            }
        }
        return Collections.min(lowVolumeNodesAbove);
    }

    private double findlvnBelow(double hvn){
        List<Double> lowVolumeNodesBelow = new ArrayList<Double>();
        for (double lvn : lowVolumeNodes) {
            if (lvn < hvn) {
                lowVolumeNodesBelow.add(lvn);
            }
        }
        return Collections.min(lowVolumeNodesBelow);
    }

    private double checkTakeProfit(double num, Side side) {
        if (side == Side.LONG) {
            return Math.min(num, this.vwap);
        } else {
            return Math.max(num, this.vwap);
        }
    }

    public void printPositions() {
        for (Position position : positions) {
            System.out.println(position.getEntry());
        }
    }
}
