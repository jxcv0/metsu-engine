package com.metsuengine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RoundingTest {
    
    @Test
    public void roundingTest() {
        double tick = 0.5;
        double num = 50000.457545;
        double roundedNum = Math.round(num / tick) * tick;

        assertEquals(50000.5, roundedNum, 0);
    }
}
