package com.metsuengine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FrameSeries implements Serializable {
    
    private int maxSize = Integer.MAX_VALUE;
    private String name = null;
    private List<Frame> series = new ArrayList<Frame>();

    public FrameSeries() {
        this.name = "unnamed";
        this.series = new ArrayList<>();
    }

    public FrameSeries(String name, List<Frame> series) {
        this.name = name;
        this.series = series;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Frame> getSeries() {
        return this.series;
    }

    public Frame getFrame(int index) {
        return this.series.get(index);
    }

    public void addFrame(Frame frame) {
        if (this.series.size() < this.maxSize) {
            this.series.add(frame);
        } else if (this.maxSize == this.series.size()) {
            Collections.sort(this.series);
            this.series.remove(Collections.min(this.series));
            this.series.add(frame);
        } 
    }

    public int getFrameCount() {
        return this.series.size();
    }

    public List<Double> getSeriesOrderBookRatio() {
        List<Double> depthRatio = new ArrayList<Double>();
        for (Frame frame : this.series) {
            depthRatio.add(frame.getOrderBook().getDeltaRatio());
        }

        return depthRatio;
    }

    public List<Double> getSeriesOrderBookDepth(String side) {
        List<Double> depth = new ArrayList<Double>();
        for (Frame frame : this.series) {
            depth.add(frame.getOrderBook().getDepth(side));
        }

        return depth;
    } 

    public List<Double> getSeriesBestBid() {
        List<Double> bestBids = new ArrayList<Double>();
        for (Frame frame : this.series) {
            bestBids.add(frame.getOrderBook().getBestBid());
        }

        return bestBids;
    }

    public List<Double> getSeriesBestAsk() {
        List<Double> bestAsks = new ArrayList<Double>();
        for (Frame frame : this.series) {
            bestAsks.add(frame.getOrderBook().getBestAsk());
        }

        return bestAsks;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public static void createTestingData(BybitEndpoint endpoint, FrameSeries frameSeries, int duration, int interval) {
        try {    
            for (int i = 0; i < duration; i++) {
                
                frameSeries.addFrame(new Frame(
                    ZonedDateTime.now(ZoneOffset.UTC),
                    endpoint.getLiquidations(ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(1)),
                    endpoint.getOrderBook()));
                
                System.out.println("getting data " + i + "/" + duration);
                
                Thread.sleep(interval);
            }

        
            FileOutputStream fileOutputStream = new FileOutputStream("BTCUSD-Bybit.txt", true);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(frameSeries);

            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FrameSeries loadFrameSeries(String file) {
        try {
            FileInputStream  fileInputStream = new FileInputStream("BTCUSD-Bybit.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            
            FrameSeries frameSeries = (FrameSeries) objectInputStream.readObject();
            objectInputStream.close();

            return frameSeries;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
