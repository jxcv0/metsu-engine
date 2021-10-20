package com.metsuengine;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TickSeriesTest {

    @Test
    public void getSubSeriesTest() {

        ZonedDateTime first = ZonedDateTime.now().minusSeconds(3);
        ZonedDateTime second = ZonedDateTime.now().minusSeconds(2);
        ZonedDateTime third = ZonedDateTime.now().minusSeconds(1);
        ZonedDateTime fourth = ZonedDateTime.now();

        TickSeries tickSeries = new TickSeries();
        tickSeries.addTick(new Tick(first, "Sell", 1000, 200));
        tickSeries.addTick(new Tick(second, "Buy", 1000.5, 100));
        tickSeries.addTick(new Tick(third, "Buy", 1000.5, 150));
        tickSeries.addTick(new Tick(fourth, "Sell", 1000, 250));
        
        List<Tick> expected1 = new ArrayList<Tick>();
        expected1.add(tickSeries.getTick(0));
        expected1.add(tickSeries.getTick(1));
        assertEquals(expected1, tickSeries.getSubSeries(0, 1));

        List<Tick> expected2 = new ArrayList<Tick>();
        expected2.add(tickSeries.getTick(2));
        expected2.add(tickSeries.getTick(3));
        assertEquals(expected2, tickSeries.getSubSeries(2, 3));
    }   
}
