package com.metsuengine;

import java.io.IOException;

import javax.websocket.Session;

public class AbstractWebSocket implements WebSocket {

    protected final TickSeries tickSeries;

    public AbstractWebSocket(TickSeries tickSeries) {
        this.tickSeries = tickSeries;
    }

    public void onOpen(Session session) {
        LOGGER.info("Connected to endpoint: " + session.getBasicRemote());
        try {
            BybitWebSocketClient.session = session;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processMessage(String message) {
        LOGGER.severe("Method not implimented");     
    }

    public void onClose(Session session) throws IOException {
        System.out.println("Disconnected");
    }

    public void processError(Throwable t) {
        t.printStackTrace();
    }
    
    public TickSeries tickSeries() {
        return tickSeries;
    }
}
