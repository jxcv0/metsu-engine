package com.metsuengine;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.metsuengine.Enums.OrderStatus;
import com.metsuengine.Enums.OrderType;
import com.metsuengine.Enums.Side;
import com.metsuengine.Enums.TimeInForce;

public class OrderMatchingStrategy implements ChangeListener {

    private static final Logger LOGGER = Logger.getLogger(OrderMatchingStrategy.class.getName());
    private Order bid;
    private Order ask;
    private BybitRestAPIClient api;
    private MarketOrderBook orderBook;
    private boolean init;

    public OrderMatchingStrategy(TickSeries tickSeries, MarketOrderBook orderBook) {
        tickSeries.addChangeListener(this);
        this.orderBook = orderBook;
        this.api = new BybitRestAPIClient("BTCUSD");
        this.init = false;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (orderBook.isReady()) {
            try {
                double bidPrice = orderBook.bestBid();
                double askPrice = orderBook.bestAsk();

                // size is only 1 usd for now
                bid = new Order("BTCUSD", Side.Buy, OrderType.Limit, bidPrice, 1, TimeInForce.GoodTillCancel, OrderStatus.New, "BID");
                ask = new Order("BTCUSD", Side.Sell, OrderType.Limit, askPrice, 1, TimeInForce.GoodTillCancel, OrderStatus.New, "ASK");

                if(!init) {
                    api.placeOrder(bid);
                    api.placeOrder(ask);
                    init = true;
                }

                List<Order> orders = api.getOrders();

                if (orders.size() < 1) {
                    api.placeOrder(bid);
                    api.placeOrder(ask);
                }

                for (Order order : orders) {
                    if (order.orderLinkId() == bid.orderLinkId()) {
                        if (order.price() != bid.price() || order.qty() != bid.qty()) {
                            api.replaceOrder(bid);
                        }
                    } else {
                        api.placeOrder(bid);
                    }

                    if (order.orderLinkId() == ask.orderLinkId()) {
                        if (order.price() != ask.price() || order.qty() != ask.qty()) {
                            api.replaceOrder(ask);
                        }
                    } else {
                        api.placeOrder(ask);
                    }
                }

            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "CANCELLING ALL ORDERS", ex);
                api.cancellAllOrders();
            }
                
        }
    }    
}
