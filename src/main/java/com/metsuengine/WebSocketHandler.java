package com.metsuengine;

import java.io.IOException;
import java.util.logging.Logger;

import javax.websocket.Session;

public interface WebSocketHandler {

    static final Logger LOGGER = Logger.getLogger(BybitAPIClient.class.getName());

    public void onOpen(Session session);

    void processMessage(String message);

    public void onClose(Session session) throws IOException;

    public void processError(Throwable t);

}
