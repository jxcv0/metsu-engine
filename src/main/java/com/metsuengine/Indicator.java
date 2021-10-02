package com.metsuengine;

import java.time.ZonedDateTime;
import java.util.HashMap;

public interface Indicator {

    void addTick(Tick tick);

    public void addTickToTimeSeries(Tick tick);

    HashMap<ZonedDateTime, Double> getTimeSeries();

    double value();
}
