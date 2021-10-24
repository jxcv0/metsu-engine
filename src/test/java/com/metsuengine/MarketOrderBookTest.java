package com.metsuengine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MarketOrderBookTest {

    @Test
    public void insertTest() {
        MarketOrderBook orderBook = new MarketOrderBook();
        orderBook.insertOrUpdate(1000, 100);
        assertEquals(100, orderBook.delta(), 0);
        orderBook.insertOrUpdate(1000.5, -50);
        assertEquals(50, orderBook.delta(), 0);
    }

    @Test
    public void bestBidAskTest() {
        MarketOrderBook orderBook = new MarketOrderBook();
        orderBook.insertOrUpdate(1000, 100);
        orderBook.insertOrUpdate(1000.5, 100);
        orderBook.insertOrUpdate(1001, 100);
        orderBook.insertOrUpdate(1001.5, -100);
        orderBook.insertOrUpdate(1002, -100);
        orderBook.insertOrUpdate(1002.5, -100);

        assertEquals(1001, orderBook.bestBid(), 0);
        assertEquals(1001.5, orderBook.bestAsk(), 0);
    }
    
    @Test
    public void totalDepthTest() {
        MarketOrderBook orderBook = new MarketOrderBook();
        orderBook.insertOrUpdate(1000, 100);
        orderBook.insertOrUpdate(1000.5, 100);
        orderBook.insertOrUpdate(1001, 100);
        orderBook.insertOrUpdate(1001.5, -100);
        orderBook.insertOrUpdate(1002, -100);
        orderBook.insertOrUpdate(1002.5, -100);
        
        assertEquals(600, orderBook.depth(), 0);

        orderBook.insertOrUpdate(999.5, 10000);

        assertEquals(10600, orderBook.depth(), 0);
    }
}
