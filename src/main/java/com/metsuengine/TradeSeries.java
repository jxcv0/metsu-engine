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
    protected LinkedList<Trade> tradeSeries;
    private EventListenerList listenerList = new EventListenerList();

    public TradeSeries() {
        this.tradeSeries = new LinkedList<Trade>();
    }

    public TradeSeries(ChangeListener listener) {
        tradeSeries = new LinkedList<Trade>();
        this.addChangeListener(listener);
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
        if (!this.tradeSeries.isEmpty()) {
            return this.tradeSeries.getLast();
        } else {
            return null;
        }
    }

    public double getSize() {
        return this.tradeSeries.size();
    }

    public void addTrade(Trade trade) {
        this.tradeSeries.add(trade);
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

    public double vwap() {
        double sumOfVolumeAtPice = 0;
        for (Trade trade : tradeSeries) {
            sumOfVolumeAtPice += (trade.price() * trade.size());
        }

        return Double.parseDouble(decimalFormat.format(sumOfVolumeAtPice / getSeriesVolume()));
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
