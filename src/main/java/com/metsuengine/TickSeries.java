package com.metsuengine;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class TickSeries {

    private String name;
    private final LinkedList<Tick> ticks;
    private final EventListenerList listenerList;
    private final int seconds;

    public TickSeries() {
        this.name = null;
        this.ticks = new LinkedList<Tick>();
        this.listenerList = new EventListenerList();
        this.seconds = Integer.MAX_VALUE;
    }

    public TickSeries(String name) {
        this.name = name;
        this.ticks = new LinkedList<Tick>();
        this.listenerList = new EventListenerList();
        this.seconds = Integer.MAX_VALUE;
    }

    public TickSeries(String name, int seconds) {
        this.name = name;
        this.ticks = new LinkedList<Tick>();
        this.listenerList = new EventListenerList();
        this.seconds = seconds;
    }

    public String getName() {
        return name;
    }

    public List<Tick> getTicks() {
        return ticks;
    }

    public Tick getTick(int index){
        return ticks.get(index);
    }

    public List<Tick> getSubSeries(int startIndex, int endIndex) {
        if (startIndex > endIndex) {
            throw new IllegalArgumentException("Start index must be before end index.");
        } else {
            List<Tick> subSeries = new ArrayList<Tick>();
            for (int i = startIndex; i <= endIndex; i++) {
                if (!ticks.get(i).equals(null)) {
                    subSeries.add(ticks.get(i)); 
                }
            }
            return subSeries;
        }
    }

    public List<Tick> getSubSeriesByTime(int seconds, int endIndex) {
        ZonedDateTime startIndex = ticks.get(endIndex).time().minusSeconds(seconds);
        List<Tick> subSeries = new ArrayList<Tick>();

        for (int i = 0; i <= endIndex; i++) {
            if (ticks.get(i) != null) {
                Tick tick = ticks.get(i);
                if (tick.time().isAfter(startIndex)) {
                    subSeries.add(tick);
                }
            }
        }

        return subSeries;
    }

    public Tick getLastTick() {
        return ticks.getLast();
    }

    public double getSize() {
        return ticks.size();
    }

    public void addTick(Tick tick) {
        ticks.add(tick);
        trimExcessValues();
        fireStateChanged();
    }

    public void addAll(List<Tick> ticks) {
        this.ticks.addAll(ticks);
    }

    public boolean isEmpty() {
        return this.getSize() < 1;
    }

    private void trimExcessValues() {
        List<Tick> toRemove = new ArrayList<Tick>();
        for (Tick tick : ticks) {
            if (tick.time().isBefore(ticks.getLast().time().minusSeconds(seconds))) {
                toRemove.add(tick);
            }
        }
        ticks.removeAll(toRemove);
    }

    public boolean contains(Tick tick) {
        return ticks.contains(tick);
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