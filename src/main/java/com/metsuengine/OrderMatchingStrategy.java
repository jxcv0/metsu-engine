package com.metsuengine;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.metsuengine.Enums.OrderType;
import com.metsuengine.Enums.Side;
import com.metsuengine.Enums.TimeInForce;
import com.metsuengine.WebSockets.BybitInversePerpetualSubscriptionSet;
import com.metsuengine.WebSockets.BybitInversePerpetualTradeWebSocket;
import com.metsuengine.WebSockets.BybitWebSocketClient;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class OrderMatchingStrategy implements ChangeListener {

    private static final Logger LOGGER = Logger.getLogger(OrderMatchingStrategy.class.getName());
    private final BybitWebSocketClient client;
    private final BybitRestAPIClient api;
    private final LimitOrderBook orderBook;
    private final DescriptiveStatistics ds = new DescriptiveStatistics();
    private final Order newBid;
    private final Order newAsk;

    public OrderMatchingStrategy(TickSeries tickSeries, LimitOrderBook orderBook) {
        listen(tickSeries);
        this.client = startClient();
        this.orderBook = orderBook;
        this.api = new BybitRestAPIClient("BTCUSD");
        this.newBid = new Order("BTCUSD", Side.Buy, OrderType.Limit, TimeInForce.GoodTillCancel);
        this.newAsk = new Order("BTCUSD", Side.Sell, OrderType.Limit, TimeInForce.GoodTillCancel);
    }

    private BybitWebSocketClient startClient() {
        return new BybitWebSocketClient(
            new BybitInversePerpetualSubscriptionSet(
                // new handler required to handle order stream
                new BybitInversePerpetualTradeWebSocket(tickSeries),
                    "wss://stream.bytick.com/realtime",
                    "trade.BTCUSD")
        );
    }

    private void listen(TickSeries tickSeries) {
        tickSeries.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        long start = System.currentTimeMillis();
        if (orderBook.isReady()) {
            try {
                double bidPrice = orderBook.bestBid();
                double askPrice = orderBook.bestAsk();

                // TODO - Order.Builder
                newBid.updatePrice(bidPrice);
                newBid.updateQty(1);
                newAsk.updatePrice(askPrice);
                newAsk.updateQty(1);

                List<Order> orders = api.getOrders();

                // if orders contains a bid - SLOOOOOOOOWWWWWWWWW
                Optional<Order> optionalBid = orders.stream().filter(o -> o.side().equals(Side.Buy)).findAny();
                if (optionalBid.isPresent()) {
                    Order currentBid = optionalBid.get();
                    // if current bid is not the name as the new calculated bid
                    if (!currentBid.isEquivalentTo(newBid)) {
                        api.replaceOrder(currentBid.orderId(), newBid);
                    }
                } else {
                    api.placeOrder(newBid);
                }

                // if orders contains an ask - ALSO SLOOOOOOOOOOWWWWWWWWW
                Optional<Order> optionalAsk = orders.stream().filter(o -> o.side().equals(Side.Sell)).findAny();
                if (optionalAsk.isPresent()) {
                    Order currentAsk = optionalAsk.get();
                    // if current ask is not the name as the new calculated bid
                    if (!currentAsk.isEquivalentTo(newAsk)) {
                        api.replaceOrder(currentAsk.orderId(), newAsk);
                    }
                } else {
                    api.placeOrder(newAsk);
                }

            } catch (IOException | InvalidKeyException | NoSuchAlgorithmException ex) {
                LOGGER.log(Level.SEVERE, "CANCELLING ALL ORDERS", ex);
                api.cancellAllOrders();
            }    
        }
        long end = System.currentTimeMillis();
        ds.addValue(end - start);
        System.out.println(ds.getMean());
    }    
}
