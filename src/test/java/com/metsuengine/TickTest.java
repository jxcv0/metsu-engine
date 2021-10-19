package com.metsuengine;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;

import org.junit.Test;

public class TickTest {

    @Test
    public void signedValueTest() {
        Tick tickBuy = new Tick(ZonedDateTime.now(), "Buy", 1000, 200);
        assertEquals("Failue", 200, tickBuy.signedValue(), 0);

        Tick tickSell = new Tick(ZonedDateTime.now(), "Sell", 1000, 350);
        assertEquals("Failue", -350, tickSell.signedValue(), 0);
    }
    
}
