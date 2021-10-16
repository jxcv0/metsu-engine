package com.metsuengine;

import static org.junit.Assert.assertArrayEquals;

import java.time.ZonedDateTime;

import org.junit.Test;

public class ToPriceArrayTest {
    
    @Test
    public void toArrayTest() {

        TickSeries tickSeries = new TickSeries();
        tickSeries.addTick(new Tick(
            ZonedDateTime.now(),
            "Sell",
            40000.5,
            1000
        ));

        tickSeries.addTick(new Tick(
            ZonedDateTime.now(),
            "Buy",
            40001,
            1000
        ));

        tickSeries.addTick(new Tick(
            ZonedDateTime.now(),
            "Sell",
            40000.5,
            1500
        ));

        double[] expecteds = {40000.5, 40001, 40000.5};
        double[] actuals = tickSeries.toPriceArray();

        assertArrayEquals(expecteds, actuals, 0);
    }
}
