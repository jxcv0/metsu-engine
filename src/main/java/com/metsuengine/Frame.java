package com.metsuengine;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Frame implements Serializable {

    private ZonedDateTime time = null;
    private List<Liquidation> liquidations = new ArrayList<>();
    private OrderBook orderBook = null;

    public Frame(ZonedDateTime time, List<Liquidation> liquidations, OrderBook orderBook) {
        this.time = time;
        this.liquidations = liquidations;
        this.orderBook = orderBook;
    }

    public ZonedDateTime getTime() {
        return this.time;
    }

    public List<Liquidation> getLiquidations() {
        return this.liquidations;
    }

    public OrderBook getOrderBook() {
        return this.orderBook;
    }

    public double getTotalLiquidations(String side) {
        double total = 0;

        for (Liquidation liquidation : this.liquidations) {
            if (liquidation.getSide().equals(side)) {
                total += liquidation.getSize();
            }
        }

        return total;
    }
}
