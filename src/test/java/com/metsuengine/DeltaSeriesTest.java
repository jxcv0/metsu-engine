package com.metsuengine;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;

import org.junit.Test;

public class DeltaSeriesTest {
    
    @Test
    public void getDeltaTest() {
        DeltaSeries deltaSeries = new DeltaSeries("test", 1000);
        deltaSeries.add(new Tick(ZonedDateTime.now().minusSeconds(2), "Buy", 1000, 1000));
        deltaSeries.add(new Tick(ZonedDateTime.now().minusSeconds(1), "Sell", 999.5, 500));
        deltaSeries.add(new Tick(ZonedDateTime.now(), "Buy", 1000, 250));
        
        assertEquals("Failure", 750, deltaSeries.getDelta(), 0);
    }

    @Test
    public void calculateTest() {
        ZonedDateTime first = ZonedDateTime.now().minusSeconds(3);
        ZonedDateTime second = ZonedDateTime.now().minusSeconds(2);
        ZonedDateTime third = ZonedDateTime.now().minusSeconds(1);
        ZonedDateTime fourth = ZonedDateTime.now();

        TickSeries tickSeries = new TickSeries();
        tickSeries.addTick(new Tick(first, "Sell", 1000, 200));
        tickSeries.addTick(new Tick(second, "Buy", 1000.5, 100));
        tickSeries.addTick(new Tick(third, "Buy", 1000.5, 150));
        tickSeries.addTick(new Tick(fourth, "Sell", 1000, 250));

        assertEquals(-200, DeltaSeries.calculate(tickSeries.getTicks()), 0);
    }
}
