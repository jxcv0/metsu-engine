package com.metsuengine;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class TickSeries extends LinkedList<Tick> {

    private EventListenerList listenerList;

    public TickSeries() {
        this.listenerList = new EventListenerList();
    }

    public TickSeries(ChangeListener listener) {
        this.addChangeListener(listener);
    }

    public LinkedList<Tick> getTrades() {
        return this;
    }

    public Tick getTrade(int index){
        return this.get(index);
    }

    public double lastTradedPrice() {
        return this.getLast().price();
    }

    public Tick getLastTrade() {
        return this.getLast();
    }

    public double getSize() {
        return this.size();
    }

    public void addTrade(Tick tick) {
        this.add(tick);
        fireStateChanged();
    }

    public void writeTradeToCSV(Tick tick) {
        String[] line = {
            tick.time().toString(),
            tick.side(),
            String.valueOf(tick.price()),
            String.valueOf(tick.size())
        };

        CSVManager manager = new CSVManager("BTCUSD-trades.csv");
        manager.writeLine(line);
    }

    public double getSeriesVolume() {
        double total = 0;
        for(Tick tick : this) {
            total += tick.size();
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        String formattedDate = formatter.format(date);

        CSVManager manager = new CSVManager(formattedDate + "-trades.csv");

        for (Tick tick : this) {
            String[] line = {
                tick.time().toString(),
                tick.side(),
                String.valueOf(tick.price()),
                String.valueOf(tick.size())
            };
            manager.writeLine(line);
        }
    }
}