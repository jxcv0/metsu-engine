package com.metsuengine;

import java.util.HashMap;
import java.util.Map;

public class MarketOrderBook {

    private final Map<Double, Integer> orderBook;

    public MarketOrderBook() {
        this.orderBook = new HashMap<Double, Integer>();
    }

    /**
     * Insert a new entry into orderbook
     * 
     * @param price the price of the order
     * @param value the signed size of the order (size * side)
     */
    public void insert(double price, int value) {
        orderBook.put(price, value);
    }

    /**
     * Update a previous entry in orderbook
     * 
     * @param price the price of the order
     * @param value the signed size of the order (size * side)
     */
    public void update(double price, int value) {
        orderBook.replace(price, value);
    }
    
    /**
     * remove a previous entry from the orderbook
     * 
     * @param price the price of the entry to remove
     */
    public void delete(double price) {
        orderBook.remove(price);
    }

    public int delta() {
        return orderBook.values().stream().mapToInt(Integer::intValue).sum();
    }

    public double bestBid() {
        return orderBook.entrySet().stream()
            .filter(entry -> entry.getValue() > 0).mapToDouble(d -> d.getKey())
            .max().getAsDouble();
    }

    public double bestAsk() {
        return orderBook.entrySet().stream()
            .filter(entry -> entry.getValue() < 0).mapToDouble(d -> d.getKey())
            .min().getAsDouble();
    }
}
