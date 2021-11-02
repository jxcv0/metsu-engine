package com.metsuengine.WebSockets;

import java.io.IOException;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metsuengine.Enums.Side;
import com.metsuengine.Position;
import com.metsuengine.WebSocketHandler;

@ClientEndpoint
public class BybitPositionWebSocket implements WebSocketHandler {

    private final Position position;

    public BybitPositionWebSocket(Position position) {
        this.position = position;
    }

    @OnOpen
    public void onOpen(Session session) {
        LOGGER.info("Connected to private endpoint: " + session.getRequestURI() + " " + session.getBasicRemote());
        try {
            BybitWebSocketClient.session = session;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void processMessage(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode response = mapper.readTree(message);

            if (response.has("data")) {
                JsonNode data = response.findValue("data");
                if (!data.isEmpty()) {
                    for (JsonNode node : data) {
                        position.setSide(Side.valueOf(node.get("side").asText()));
                        position.setSize(node.get("size").asDouble());
                        position.setEntryPrice(node.get("entry_price").asDouble());
                    }

                }
            }
  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        LOGGER.info("Disconnected");
    }

    @OnError
    public void processError(Throwable t) {
        t.printStackTrace();
    }
}