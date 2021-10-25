package com.metsuengine.WebSockets;

import com.metsuengine.WebSocketHandler;

public class CoinbaseSpotSubscriptionSet {

    private WebSocketHandler handler;
    private String productId;
    private String channel;

    public CoinbaseSpotSubscriptionSet(WebSocketHandler handler, String productId, String channel) {
        this.handler = handler;
        this.productId = productId;
        this.channel = channel;
    }

    public WebSocketHandler getHandler() {
        return handler;
    }

    public String getProductId() {
        return productId;
    }

    public String getChannel() {
        return channel;
    }
}
