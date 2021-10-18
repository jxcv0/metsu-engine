package com.metsuengine;

import java.time.ZonedDateTime;

import org.junit.Test;

public class VolumeDistributionTest {
    
    @Test
    public void toPriceArrayTest() {
        TickSeries tickSeries = new TickSeries();
        TickDistribution tickDistribution = new TickDistribution("vd", tickSeries);
        tickSeries.addTick(new Tick(ZonedDateTime.now(), "Buy", 1000, 1000));
        tickSeries.addTick(new Tick(ZonedDateTime.now(), "Buy", 1000, 1000));
        tickSeries.addTick(new Tick(ZonedDateTime.now(), "Buy", 1000, 1000));
    }
}
