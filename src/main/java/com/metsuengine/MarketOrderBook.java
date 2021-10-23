package com.metsuengine;

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class MarketOrderBook {
    
    private final Map<Double, Integer> orderBook;
    private final EventListenerList listenerList;

    public MarketOrderBook() {
        this.orderBook = new HashMap<Double, Integer>();
        this.listenerList = new EventListenerList();
    }

    /**
     * Inserts a new entry into orderbook. Overwrites previous entry
     * 
     * @param price the price of the order
     * @param value the signed size of the order (size * side)
     */
    public void insertOrUpdate(double price, int value) {
        orderBook.put(price, value);
        fireStateChanged();
    }
    
    /**
     * remove a previous entry from the orderbook
     * 
     * @param price the price of the entry to remove
     */
    public void delete(double price) {
        orderBook.remove(price);
        fireStateChanged();
    }

    public int delta() {
        return orderBook.values().stream().mapToInt(Integer::intValue).sum();
    }

    public double bestBid() {
        return orderBook.entrySet().stream()
            .filter(entry -> entry.getValue() > 0).mapToDouble(d -> d.getKey())
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

    public Map<Double, Integer> map() {
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
