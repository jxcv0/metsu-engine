package com.metsuengine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GlostenMilgromTest {

    @Test
    public void expectedAssetValueAfterBuyTest() {
        GlostenMilgrom gm = new GlostenMilgrom(0.2, 0.5);

        assertEquals(50064, gm.expectedAssetValueAfterBuy(50100, 50010), 0);
        assertEquals(50046, gm.expectedAssetValueAfterSell(50100, 50010), 0);
    }
}
