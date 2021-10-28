package com.metsuengine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class MarketOrderBook {
    
    private final Map<Double, Double> orderBook;
    private final EventListenerList listenerList;
    private boolean ready;

    public MarketOrderBook() {
        this.orderBook = new ConcurrentHashMap<Double, Double>();
        this.listenerList = new EventListenerList();
        this.ready = false;
    }

    public void ready() {
        ready = true; 
    }

    public boolean isReady() {
        return ready;
    }

    /**
     * Inserts a new entry into orderbook. Overwrites previous entry.
     * If value is 0 then the entry is removed.
     * 
     * @param price the price of the order.
     * @param value the signed size of the order (size * side).
     */
    public void insertOrUpdate(double price, double value) {
        if (value == 0) {
            delete(price);
        } else {
            // System.out.println("Inserting: " + price + " " + value);
            orderBook.put(price, value);
            fireStateChanged();
        }
    }
    
    /**
     * remove an entry from the orderbook.
     * 
     * @param price the price of the entry to remove.
     */
    public void delete(double price) {
        // System.out.println("Deleting: " + price);
        orderBook.remove(price);
        fireStateChanged();
    }

    public double getVolumeAtLevel(double level) {
        return orderBook.get(level);
    }

    public double delta() {
        return orderBook.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public double delta(double cuttoff) {
        double upperBound = bestAsk() * (1 + cuttoff);
        double lowerBound = bestBid() * (1 - cuttoff);


        return orderBook.entrySet().stream()
            .filter(x -> x.getKey() < upperBound)
            .filter(x -> x.getKey() > lowerBound)
            .mapToDouble(x -> x.getValue()).sum();
    }

    public double depth() {
        return orderBook.values().stream().map(n -> Math.abs(n))
            .collect(Collectors.summingDouble(n -> n));
    }

    public double deltaRatio() {
        return delta()/depth();
    }

    public double bestBid() {
        return orderBook.entrySet().stream()
            .filter(x -> x.getValue() > 0).mapToDouble(x -> x.getKey())
            .max().getAsDouble(); 
    }

    public double bestAsk() {
        return orderBook.entrySet().stream()
            .filter(entry -> entry.getValue() < 0).mapToDouble(d -> d.getKey())
            .min().getAsDouble();
    }

    public int size() {
        return orderBook.size();
    }

    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    public Map<Double, Double> map() {
        return orderBook;
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
}
