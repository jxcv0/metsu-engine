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
import com.metsuengine.Tick;
import com.metsuengine.TickSeries;
import com.metsuengine.WebSocketHandler;

@ClientEndpoint
public class BybitInversePerpetualTradeWebSocket implements WebSocketHandler {

    private final TickSeries tickSeries;

    public BybitInversePerpetualTradeWebSocket(TickSeries tickSeries) {
        this.tickSeries = tickSeries;
    }

    @OnOpen
    public void onOpen(Session session) {
        LOGGER.info("Connected to Inverse Perpetual endpoint: " + session.getRequestURI() + " " + session.getBasicRemote());
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

            System.out.println(response);

            if (response.has("data")) {
                JsonNode data = response.findValue("data");

                if (!data.isNull()) {
                    Tick tick = new Tick(
                        data.findValue("timestamp").asText(),
                        data.findValue("side").asText(),
                        data.findValue("price").asDouble(),
                        data.findValue("size").asDouble());
                    
                    tickSeries.addTick(tick);
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

    public TickSeries tickSeries() {
        return tickSeries;
    }
}