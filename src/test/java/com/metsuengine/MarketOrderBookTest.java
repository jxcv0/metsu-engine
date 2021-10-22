package com.metsuengine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MarketOrderBookTest {

    @Test
    public void totalDepthTest() {
        MarketOrderBook orderBook = new MarketOrderBook();
        orderBook.insert(1000, 100);
        assertEquals(100, orderBook.delta(), 0);
        orderBook.insert(1000.5, -50);
        assertEquals(50, orderBook.delta(), 0);
    }

    @Test
    public void bestBidAskTest() {
        MarketOrderBook orderBook = new MarketOrderBook();
        orderBook.insert(1000, 100);
        orderBook.insert(1000.5, 100);
        orderBook.insert(1001, 100);
        orderBook.insert(1001.5, -100);
        orderBook.insert(1002, -100);
        orderBook.insert(1002.5, -100);

        assertEquals(1001, orderBook.bestBid(), 0);
        assertEquals(1001.5, orderBook.bestAsk(), 0);
    }
    
}
