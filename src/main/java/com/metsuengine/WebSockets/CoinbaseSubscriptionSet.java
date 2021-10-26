package com.metsuengine.WebSockets;

import com.metsuengine.WebSocketHandler;

public class CoinbaseSubscriptionSet {

    private WebSocketHandler handler;
    private String productId;
    private String[] channels;

    public CoinbaseSubscriptionSet(WebSocketHandler handler, String productId, String[] channels) {
        this.handler = handler;
        this.productId = productId;
        this.channels = channels;
    }

    public WebSocketHandler getHandler() {
        return handler;
    }

    public String getProductId() {
        return productId;
    }

    public String[] getChannels() {
        return channels;
    }
}
