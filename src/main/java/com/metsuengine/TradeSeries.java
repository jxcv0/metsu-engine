package com.metsuengine;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class TradeSeries implements Serializable {

    private LinkedList<Trade> tradeSeries;
    private EventListenerList listenerList = new EventListenerList();
    private VolumeProfile volumeProfile;

    public TradeSeries() {
        tradeSeries = new LinkedList<Trade>();
        volumeProfile = new VolumeProfile();
    }

    public TradeSeries(LinkedList<Trade> tradeSeries) {
        this.tradeSeries = tradeSeries;
        for (Trade trade : tradeSeries) {
            this.volumeProfile.add(trade);
        }
    }

    public LinkedList<Trade> getTrades() {
        return this.tradeSeries;
    }

    public Trade getTrade(int index){
        return this.tradeSeries.get(index);
    } 

    public Trade getLastTrade() {
        if (!this.tradeSeries.isEmpty()) {
            return this.tradeSeries.getLast();
        } else {
            return null;
        }
    }

    public double getSize() {
        return this.tradeSeries.size();
    }

    public void addTrade(Trade trade) {
        this.tradeSeries.add(trade);
        this.volumeProfile.add(trade);
        fireStateChanged();
    }

    public void writeTradeToCSV(Trade trade) {
        String[] line = {
            trade.getTime().toString(),
            trade.getSide(),
            String.valueOf(trade.getPrice()),
            String.valueOf(trade.getSize())
        };

        CSVManager manager = new CSVManager("BTCUSD-trades.csv");
        manager.writeLine(line);
    }

    public HashMap<Double, Double> createMap(TradeSeries tradeSeries) {

        // TODO is there a better way?
        HashMap<Double, Double> map = new HashMap<Double, Double>();

        for (Trade trade : this.tradeSeries) {
            if (!map.containsKey(trade.getPrice())) {
                map.put(trade.getPrice(), trade.getSize());
            } else {
                double volume = map.get(trade.getPrice());
                map.put(trade.getPrice(), volume + trade.getSize());
            }
        }
        return map;
    }

    public double calculateVWAP() {
        double sumOfVolumeAtPice = 0;
        for (Trade trade : tradeSeries) {
            sumOfVolumeAtPice += (trade.getPrice() * trade.getSize());
        }
        return (sumOfVolumeAtPice / getSeriesVolume());
    }

    public double getSeriesVolume() {
        double total = 0;
        for(Trade trade : this.tradeSeries) {
            total += trade.getSize();
        }
        return total;
    }

    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    protected void fireStateChanged() {
        ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);
        if (listeners != null && listeners.length > 0) {
            ChangeEvent event = new ChangeEvent(this);
            for (ChangeListener listener : listeners) {
                listener.stateChanged(event);
            }
        }
    }

    public void writeAndPurge(ZonedDateTime date) {

        this.volumeProfile.writeVolumeProfile(date); // this aint right

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        String formattedDate = formatter.format(date);

        CSVManager manager = new CSVManager(formattedDate + "-trades.csv");

        for (Trade trade : tradeSeries) {
            String[] line = {
                trade.getTime().toString(),
                trade.getSide(),
                String.valueOf(trade.getPrice()),
                String.valueOf(trade.getSize())
            };
            manager.writeLine(line);
        }
        
        this.purge();
    }

    public void purge() {
        this.tradeSeries = new LinkedList<Trade>();
        this.volumeProfile = new VolumeProfile();
    }
}
