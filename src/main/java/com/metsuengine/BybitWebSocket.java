package com.metsuengine;

import java.io.IOException;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ClientEndpoint
public class BybitWebSocket {

    private TradeSeries tradeSeries;

    public BybitWebSocket() {
        this.tradeSeries = new TradeSeries();
    }

    public BybitWebSocket(TradeSeries tradeSeries) {
        this.tradeSeries = tradeSeries;
    }

    public BybitWebSocket(int maxSize) {
        this.tradeSeries.setMaxSize(maxSize);
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
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

                if (!data.isNull()) {
                    Trade trade = new Trade(
                        data.findValue("timestamp").asText(),
                        data.findValue("side").asText(),
                        data.findValue("price").asDouble(),
                        data.findValue("size").asDouble());
                    
                    // fires change
                    tradeSeries.addTrade(trade);
                }
            }
  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("Disconnected");
    }

    @OnError
    public void processError(Throwable t) {
        t.printStackTrace();
    }
}