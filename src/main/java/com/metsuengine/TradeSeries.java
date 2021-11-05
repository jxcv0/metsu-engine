package com.metsuengine;

import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class TradeSeries {

    private final LinkedList<Trade> trades;
    private final EventListenerList listenerList;
    private final int seconds;

    public TradeSeries() {
        this.trades = new LinkedList<Trade>();
        this.listenerList = new EventListenerList();
        this.seconds = Integer.MAX_VALUE;
    }

    public TradeSeries(int seconds) {
        this.trades = new LinkedList<Trade>();
        this.listenerList = new EventListenerList();
        this.seconds = seconds;
    }

    public void addtrade(Trade trade) {
        trades.add(trade);
        trimExcessValues();
        fireStateChanged();
    }

    public double delta() {
        return trades.stream().mapToDouble(t -> t.signedVolume()).sum();
    }

    public double volume() {
        return trades.stream().mapToDouble(t -> t.size()).sum();
    }

    public double deltaRatio() {
        return delta()/volume();
    }

    public List<Trade> gettrades() {
        return trades;
    }

    public double[] toArray() {
        return trades.stream().mapToDouble(t -> t.price()).toArray();
    }

    public Trade lasttrade() {
        return trades.getLast();
    }

    public double size() {
        return trades.size();
    }

    public void addAll(List<Trade> trades) {
        this.trades.addAll(trades);
    }

    public boolean isEmpty() {
        return trades.isEmpty();
    }

    private void trimExcessValues() {
        for (Iterator<Trade> t = trades.iterator(); t.hasNext();) {
            Trade trade = t.next();
            ZonedDateTime cutoff = trades.getLast().time().minusSeconds(seconds);
            if (trade.time().isBefore(cutoff)) {
                t.remove();
            }
        }
    }

    public boolean contains(Trade trade) {
        return trades.contains(trade);
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
}