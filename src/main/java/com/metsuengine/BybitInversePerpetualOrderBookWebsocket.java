package com.metsuengine;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ClientEndpoint
public class BybitInversePerpetualOrderBookWebsocket implements WebSocket{
    
    private final MarketOrderBook orderBook;

    public BybitInversePerpetualOrderBookWebsocket(MarketOrderBook orderBook) {
        this.orderBook = orderBook;
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
    public void proccessMessage(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode response = mapper.readTree(message);

            if (response.has("type")) {
                JsonNode data = response.findValue("data");

                if (data)

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
}
