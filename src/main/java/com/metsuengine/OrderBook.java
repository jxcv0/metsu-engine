package com.metsuengine;

import java.util.Collections;
import java.util.HashMap;

public class OrderBook {

    private String name;
    private HashMap<Double, Integer> orderBookBid = new HashMap<Double, Integer>();
    private HashMap<Double, Integer> orderBookAsk = new HashMap<Double, Integer>();

    public OrderBook(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void deleteBidLevel(double price) {
        this.orderBookBid.remove(price);
    }

    public void deleteAskLevel(double price) {
        this.orderBookAsk.remove(price);
    }

    public void updateBidLevel(double price, int size) {
        this.orderBookBid.replace(price, size);
    }

    public void updateAskLevel(double price, int size) {
        this.orderBookAsk.replace(price, size);
    }

    public void insertBidLevel(double price, int size) {
        this.orderBookBid.put(price, size);
    }

    public void insertAskLevel(double price, int size) {
        this.orderBookAsk.put(price, size);
    }

    public int getTotalDepth() {
        int buyVolume = 0;
        int sellVolume = 0;

        for (Double level : this.orderBookBid.keySet()) {
            buyVolume += orderBookBid.get(level);
        }

        for (Double level : this.orderBookAsk.keySet()) {
            sellVolume += orderBookAsk.get(level);
        }

        return buyVolume + sellVolume;
    }

    public double getDepth(String side) {
        int depth = 0;
        if (side.equals("Buy")) {
            for (double level : orderBookBid.keySet()) {
                depth += orderBookBid.get(level);
            }
            return depth;

        } else {
            for (double level : orderBookAsk.keySet()) {
                depth += orderBookAsk.get(level);
            }
            
            return depth;
        }
    }

    public double getOrderBookDelta() {
        int buyVolume = 0;
        int sellVolume = 0;
        double orderBookDelta = 0;

        for (Double level : this.orderBookBid.keySet()) {
            buyVolume += orderBookBid.get(level);
        }

        for (Double level : this.orderBookAsk.keySet()) {
            sellVolume += orderBookAsk.get(level);
        }
        
        orderBookDelta = buyVolume - sellVolume;

        return orderBookDelta;
    }

    public double getDeltaRatio() {
        double total = this.getTotalDepth();
        double delta = this.getOrderBookDelta();

        return (delta/total)*100;
    }

    public double getBestBid() {
        double bestBid = 0;
        
        if (!this.orderBookBid.keySet().isEmpty()) {
            bestBid = Collections.max(this.orderBookBid.keySet());
        }
        
        return bestBid;
    }

    public double getBestAsk() {
        double bestAsk = 0;
        
        if (!this.orderBookAsk.keySet().isEmpty()) {
            bestAsk = Collections.min(this.orderBookAsk.keySet());
        }
        
        return bestAsk;
    }

    public double getSpread() {
        return this.getBestAsk() - this.getBestBid();
    }

    public void displaySizes() {
        System.out.println("Bid: " + this.orderBookBid.size());
        System.out.println("Ask: " + this.orderBookAsk.size());
    }
    
}
