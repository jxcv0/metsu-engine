package com.metsuengine;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;

import org.junit.Test;

public class TradeTest {

    @Test
    public void signedValueTest() {
        Trade tradeBuy = new Trade(ZonedDateTime.now(), "Buy", 1000, 200);
        assertEquals("Failure", 200, tradeBuy.signedVolume(), 0);

        Trade tradeSell = new Trade(ZonedDateTime.now(), "Sell", 1000, 350);
        assertEquals("Failure", -350, tradeSell.signedVolume(), 0);
    }    
}
