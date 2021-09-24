package com.metsuengine;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class TradeSeries implements Serializable {

    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private LinkedList<Trade> tradeSeries;
    private Trade lastTrade;
    private EventListenerList listenerList = new EventListenerList();
    private VWAP vwap;

    public TradeSeries() {
        this.tradeSeries = new LinkedList<Trade>();
        this.vwap = new VWAP();
    }

    public TradeSeries(ChangeListener listener) {
        tradeSeries = new LinkedList<Trade>();
        this.addChangeListener(listener);
        this.vwap = new VWAP();
    }

    public void setSeries(LinkedList<Trade> tradeSeries) {
        this.tradeSeries = tradeSeries;
    }

    public LinkedList<Trade> getTrades() {
        return this.tradeSeries;
    }

    public Trade getTrade(int index){
        return this.tradeSeries.get(index);
    } 

    public Trade getLastTrade() {
        return this.lastTrade;
    }

    public double getSize() {
        return this.tradeSeries.size();
    }

    public void addTrade(Trade trade) {
        this.lastTrade = trade;
        this.tradeSeries.add(trade);
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
        for(Trade trade : this.tradeSeries) {
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

    public double vwap() {
        return this.vwap.value();
    }

    public void writeAndPurge(ZonedDateTime date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        String formattedDate = formatter.format(date);

        CSVManager manager = new CSVManager(formattedDate + "-trades.csv");

        for (Trade trade : tradeSeries) {
            String[] line = {
                trade.time().toString(),
                trade.side(),
                String.valueOf(trade.price()),
                String.valueOf(trade.size())
            };
            manager.writeLine(line);
        }
        
        this.purge();
    }

    public void purge() {
        this.tradeSeries = new LinkedList<Trade>();
    }
}
