package com.metsuengine;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class TickSeries {

    private final LinkedList<Tick> ticks;
    private final EventListenerList listenerList;

    public TickSeries() {
        this.ticks = new LinkedList<Tick>();
        this.listenerList = new EventListenerList();
    }

    public List<Tick> getTicks() {
        return ticks;
    }

    public Tick getTick(int index){
        return ticks.get(index);
    }

    public Tick getLastTick() {
        return ticks.getLast();
    }

    public double getSize() {
        return ticks.size();
    }

    public void addTick(Tick tick) {
        ticks.add(tick);
        fireStateChanged();
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