package com.metsuengine;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;

import org.junit.Test;

public class TickDistributionTest {
    
    @Test
    public void toPriceArrayTest() {
        TradeSeries tickSeries = new TradeSeries();
        TickDistribution distribution = new TickDistribution("vd", tickSeries, 20);
        tickSeries.addTick(new Trade(ZonedDateTime.now(), "Buy", 1000, 1000));
        tickSeries.addTick(new Trade(ZonedDateTime.now(), "Sell", 1000, 1000));
        tickSeries.addTick(new Trade(ZonedDateTime.now(), "Buy", 900, 1000));

        assertEquals(2000, distribution.getVolumeAtPrice(1000), 0);
        assertEquals(1000, distribution.getVolumeAtPrice(900), 0);
        assertEquals(0, distribution.getDeltaAtPrice(1000), 0);
    }
}
