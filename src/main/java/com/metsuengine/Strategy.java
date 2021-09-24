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
    private VWAP vwap;
    private boolean initialized;

    public Strategy(List<Double> highVolumeNodes, List<Double> lowVolumeNodes, VWAP vwap) {
        this.highVolumeNodes = highVolumeNodes;
        this.lowVolumeNodes = lowVolumeNodes;
        this.positions = new ArrayList<Position>();
        this.vwap = vwap;
        this.initialized = false;
    }

    public void update(Trade lastTrade) {
        this.lastTrade = lastTrade;
        if (this.lastTrade != null) {
            initializePositions();
            this.initialized = true; 
        }
        // check TPs
        // check if a position is fillable
        // if fillable, fill check if closable
        // if closeable calculate returns and save pnl, check if reversable
        // if reversable make new position
    }

    public void initializePositions() { 
        for (double hvn : highVolumeNodes) {
            if (lastTrade.price() > hvn) {
                positions.add(new Position(
                    count.incrementAndGet(),
                    Side.LONG, 
                    hvn, 
                    findlvnAbove(hvn),
                    checkTakeProfit(findlvnBelow(hvn), Side.SHORT)));
            } else {
                positions.add(new Position(
                    count.incrementAndGet(),
                    Side.SHORT, 
                    hvn, 
                    findlvnAbove(hvn),
                    checkTakeProfit(findlvnBelow(hvn), Side.LONG)));
            }
        }
        if (this.initialized == false) {
            printPositions();
        }
    }

    private double findlvnAbove(double hvn){
        List<Double> lowVolumeNodesAbove = new ArrayList<Double>();
        for (double lvn : lowVolumeNodes) {
            if (lvn > hvn) {
                lowVolumeNodesAbove.add(lvn);
            }
        }

        if (lowVolumeNodesAbove.size() < 1) {
            return round(hvn * 1.05);
        } else {
            return Collections.min(lowVolumeNodesAbove);
        }
    }

    private double findlvnBelow(double hvn){
        List<Double> lowVolumeNodesBelow = new ArrayList<Double>();
        for (double lvn : lowVolumeNodes) {
            if (lvn < hvn) {
                lowVolumeNodesBelow.add(lvn);
            }
        }

        if (lowVolumeNodesBelow.size() < 1) {
            return round(hvn * 0.95);
        } else {
            return Collections.max(lowVolumeNodesBelow);

        }
    }

    private double round(double num) {
        return Math.round(num * 2) / 2.0;
    }

    private double checkTakeProfit(double num, Side side) {
        if (side == Side.LONG) {
            return Math.min(num, vwap.value());
        } else {
            return Math.max(num, vwap.value());
        }
    }

    public void printPositions() {
        System.out.println("Time: " + this.lastTrade.time() + "\tLTP: " + this.lastTrade.price() + "\tVWAP: " + this.vwap);
        for (Position position : positions) {
            System.out.println(
                "Side: " + position.getSide() +
                "  Entry: " + position.getEntry() +
                "  SL: " + position.getStopLoss() +
                "  TP: " + position.getTakeProfit());
        }
    }

    public List<Position> getPositions() {
        return this.positions;
    }
}
