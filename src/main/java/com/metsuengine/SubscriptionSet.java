package com.metsuengine;

public class SubscriptionSet {
    
    private WebSocket webSocket;
    private String uri;
    private String topic;

    public SubscriptionSet(WebSocket webSocket, String uri, String topic) {
        this.webSocket = webSocket;
        this.uri = uri;
        this.topic = topic;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public String getURI() {
        return uri;
    }

    public String getTopic() {
        return topic;
    }
}
