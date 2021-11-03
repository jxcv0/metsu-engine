package com.metsuengine;

import java.time.ZonedDateTime;
import java.util.Iterator;
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

    public void addTick(Tick tick) {
        ticks.add(tick);
        trimExcessValues();
        fireStateChanged();
    }

    public double delta() {
        return ticks.stream().mapToDouble(t -> t.signedVolume()).sum();
    }

    public double volume() {
        return ticks.stream().mapToDouble(t -> t.size()).sum();
    }

    public double deltaRatio() {
        return delta()/volume();
    }

    public List<Tick> getTicks() {
        return ticks;
    }

    public double[] toArray() {
        return ticks.stream().mapToDouble(t -> t.price()).toArray();
    }

    public Tick lastTick() {
        return ticks.getLast();
    }

    public double size() {
        return ticks.size();
    }

    public void addAll(List<Tick> ticks) {
        this.ticks.addAll(ticks);
    }

    public boolean isEmpty() {
        return ticks.isEmpty();
    }

    private void trimExcessValues() {
        for (Iterator<Tick> t = ticks.iterator(); t.hasNext();) {
            Tick tick = t.next();
            ZonedDateTime cutoff = ticks.getLast().time().minusSeconds(seconds);
            if (tick.time().isBefore(cutoff)) {
                t.remove();
            }
        }
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