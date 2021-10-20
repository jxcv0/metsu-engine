package com.metsuengine;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;

import org.junit.Test;

public class TickTest {

    @Test
    public void signedValueTest() {
        Tick tickBuy = new Tick(ZonedDateTime.now(), "Buy", 1000, 200);
        assertEquals("Failure", 200, tickBuy.signedVolume(), 0);

        Tick tickSell = new Tick(ZonedDateTime.now(), "Sell", 1000, 350);
        assertEquals("Failure", -350, tickSell.signedVolume(), 0);
    }    
}
