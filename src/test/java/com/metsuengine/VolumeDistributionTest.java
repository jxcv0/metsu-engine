package com.metsuengine;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;

import org.junit.Test;

public class VolumeDistributionTest {
    
    @Test
    public void toPriceArrayTest() {
        TickSeries tickSeries = new TickSeries();
        TickDistribution distribution = new TickDistribution("vd", tickSeries);
        tickSeries.addTick(new Tick(ZonedDateTime.now(), "Buy", 1000, 1000));
        tickSeries.addTick(new Tick(ZonedDateTime.now(), "Buy", 1000, 1000));
        tickSeries.addTick(new Tick(ZonedDateTime.now(), "Buy", 900, 1000));

        assertEquals(2000, distribution.getVolumeAtPrice(1000), 0);
        assertEquals(1000, distribution.getVolumeAtPrice(900), 0);
    }
}
