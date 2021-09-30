package com.metsuengine;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class TradeSeries extends LinkedList<Trade> {

    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private EventListenerList listenerList = new EventListenerList();
    private final VWAP vwap;

    public TradeSeries() {
        this.vwap = new VWAP();
    }

    public TradeSeries(ChangeListener listener) {
        this.addChangeListener(listener);
        this.vwap = new VWAP();
    }

    public LinkedList<Trade> getTrades() {
        return this;
    }

    public Trade getTrade(int index){
        return this.get(index);
    }

    public double lastTradedPrice() {
        return this.getLast().price();
    }

    public Trade getLastTrade() {
        return this.getLast();
    }

    public double getSize() {
        return this.size();
    }

    public void addTrade(Trade trade) {
        this.add(trade);
        vwap.increment(trade);
        fireStateChanged();
    }

    public void writeTradeToCSV(Trade trade) {
        String[] line = {
            trade.time().toString(),
            trade.side(),
            String.valueOf(trade.price()),
            String.valueOf(trade.size())
        };

        CSVManager manager = new CSVManager("BTCUSD-trades.csv");
        manager.writeLine(line);
    }

    public double getSeriesVolume() {
        double total = 0;
        for(Trade trade : this) {
            total += trade.size();
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

    public VWAP vwap() {
        return this.vwap;
    }

    public void writeAndPurge(ZonedDateTime date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        String formattedDate = formatter.format(date);

        CSVManager manager = new CSVManager(formattedDate + "-trades.csv");

        for (Trade trade : this) {
            String[] line = {
                trade.time().toString(),
                trade.side(),
                String.valueOf(trade.price()),
                String.valueOf(trade.size())
            };
            manager.writeLine(line);
        }
    }
}