package com.metsuengine;

import java.util.logging.Logger;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

public class BybitWebSocketEndpoint extends Endpoint {

    static final Logger LOGGER = Logger.getLogger(BybitWebSocketEndpoint.class.getName());

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        LOGGER.info("Connected to endpoint: " + session.getRequestURI());
        try {
            BybitWebSocketClient.session = session;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
