package com.metsuengine;

import java.time.ZonedDateTime;
import java.util.HashMap;

public interface Indicator {

    void addTrade(Trade trade);

    public void addTradeToTimeSeries(Trade trade);

    HashMap<ZonedDateTime, Double> getTimeSeries();

    double value();
}
