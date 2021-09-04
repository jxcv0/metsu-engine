package com.metsuengine;

import java.util.ArrayList;
import java.util.List;

public class Snapshot {

    List<Liquidation> liquidations = new ArrayList<>();
    OrderBook orderBook = null;

    public Snapshot(List<Liquidation> liquidations, OrderBook orderBook) {
        this.liquidations = liquidations;
        this.orderBook = orderBook;
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
