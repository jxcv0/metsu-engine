package com.metsuengine;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;

import org.junit.Test;

public class DeltaSeriesTest {
    
    @Test
    public void getDeltaTest() {
        DeltaSeries deltaSeries = new DeltaSeries("test", 1000);
        deltaSeries.addTick(new Tick(ZonedDateTime.now().minusSeconds(2), "Buy", 1000, 1000));
        deltaSeries.addTick(new Tick(ZonedDateTime.now().minusSeconds(1), "Sell", 999.5, 500));
        deltaSeries.addTick(new Tick(ZonedDateTime.now(), "Buy", 1000, 250));
        
        assertEquals("Failure", 750, deltaSeries.getDelta(), 0);
    }
}
