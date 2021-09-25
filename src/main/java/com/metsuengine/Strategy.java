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

        if (this.initialized == true) {
            managePositions();
        }
        // check if reversable
        // if reversable make new position
    }

    public void initializePositions() { 
        for (double hvn : highVolumeNodes) {
            if (lastTrade.price() > hvn) {
                positions.add(new Position(
                    count.incrementAndGet(),
                    Side.LONG, 
                    hvn, 
                    findlvnBelow(hvn),
                    checkTakeProfit(hvn, findlvnAbove(hvn), Side.SHORT)));
            } else {
                positions.add(new Position(
                    count.incrementAndGet(),
                    Side.SHORT, 
                    hvn, 
                    findlvnAbove(hvn),
                    checkTakeProfit(hvn, findlvnBelow(hvn), Side.LONG)));
            }
        }
        if (this.initialized == false) {
            printPositions();
        }
    }

    public void managePositions() {
        for (Position position : positions) {
            if (!position.isClosed() && position.isFilled()) {
                checkCloseable(position);
                checkTakeProfit(position.getEntry(), position.getTakeProfit(), position.getSide());
            } else {
                // TODO ltp needs to be >=1 tick past entry price for fill
                if (lastTrade.price() == position.getEntry()) {
                    position.fill();
                }
            }
        }
    }

    public void checkCloseable(Position position) {
        if (lastTrade.price() == position.getStopLoss() || lastTrade.price() == position.getTakeProfit()) {
            position.close(lastTrade.price());
            System.out.println("Position closed at " + lastTrade.price());
            System.out.println("PnL: " + position.pnl());
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
            return round(hvn * 1.02);
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
            return round(hvn * 0.98);
        } else {
            return Collections.max(lowVolumeNodesBelow);

        }
    }

    private double round(double num) {
        return Math.round(num * 2) / 2.0;
    }

    private double checkTakeProfit(double entry, double exit, Side side) {
        if (side == Side.LONG && vwap.value() > entry) {
            return Math.min(exit, vwap.value());
        } else if (side == Side.SHORT && vwap.value() < entry) {
            return Math.max(exit, vwap.value());
        } else {
            return exit;
        }
    }

    public void printPositions() {
        System.out.println("Time: " + this.lastTrade.time() + "\tLTP: " + this.lastTrade.price() + "\tVWAP: " + this.vwap.value());
        for (Position position : positions) {
            System.out.println(
                "Side: " + position.getSide() +
                "  Entry: " + position.getEntry() +
                "  SL: " + position.getStopLoss() +
                "  TP: " + position.getTakeProfit());
        }
    }

    public void returns() {
        double totalPnL = 0;
        for (Position position : positions) {
            totalPnL += position.pnl();
        }
        System.out.println(totalPnL);
    }

    public List<Position> getPositions() {
        return this.positions;
    }
}
