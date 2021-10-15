package com.metsuengine;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class TickSeries {

    private String name;
    private final LinkedList<Tick> ticks;
    private final EventListenerList listenerList;
    private final int maxSize;

    public TickSeries() {
        this.name = null;
        this.ticks = new LinkedList<Tick>();
        this.listenerList = new EventListenerList();
        this.maxSize = Integer.MAX_VALUE;
    }

    public TickSeries(String name) {
        this.name = name;
        this.ticks = new LinkedList<Tick>();
        this.listenerList = new EventListenerList();
        this.maxSize = Integer.MAX_VALUE;
    }

    public String getname() {
        return name;
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
        trimExcessValues();
        fireStateChanged();
    }

    public boolean isEmpty() {
        return this.getSize() < 1;
    }

    private void trimExcessValues() {
        if (ticks.size() > maxSize) {
            ticks.removeLast();
        }
    }

    public Double[] toArray() {
        return ticks.toArray(new Double[0]);
    }

    public Double[] toArray(LinkedList<Double> doubleLinkedList) {
        Double[] doubleArray = new Double[doubleLinkedList.size()];
        return doubleLinkedList.toArray(doubleArray);
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