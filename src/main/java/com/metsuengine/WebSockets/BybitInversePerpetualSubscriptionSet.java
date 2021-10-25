package com.metsuengine.WebSockets;

import com.metsuengine.WebSocketHandler;

public class BybitInversePerpetualSubscriptionSet {
    
    private WebSocketHandler handler;
    private String uri;
    private String topic;

    public BybitInversePerpetualSubscriptionSet(WebSocketHandler handler, String uri, String topic) {
        this.handler = handler;
        this.uri = uri;
        this.topic = topic;
    }

    public WebSocketHandler getHandler() {
        return handler;
    }

    public String getURI() {
        return uri;
    }

    public String getTopic() {
        return topic;
    }
}
