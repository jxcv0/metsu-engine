package com.metsuengine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class TickSeries {

    private final LinkedList<Tick> ticks;
    private final EventListenerList listenerList;
    private final int seconds;

    public TickSeries() {
        this.ticks = new LinkedList<Tick>();
        this.listenerList = new EventListenerList();
        this.seconds = Integer.MAX_VALUE;
    }

    public TickSeries(int seconds) {
        this.ticks = new LinkedList<Tick>();
        this.listenerList = new EventListenerList();
        this.seconds = seconds;
    }

    public List<Tick> getTicks() {
        return ticks;
    }

    public Tick lastTick() {
        return ticks.getLast();
    }

    public double size() {
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
        return ticks.isEmpty();
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