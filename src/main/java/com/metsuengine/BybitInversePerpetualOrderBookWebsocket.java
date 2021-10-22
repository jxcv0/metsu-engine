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
    public void processMessage(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode response = mapper.readTree(message);

            if (response.has("type")) {
                JsonNode type = response.get("type");
                if (type.asText().equals("snapshot")) {
                    JsonNode data = response.get("data");
    
                    for (JsonNode node : data) {
                        double price = Double.parseDouble(node.get("price").asText());
                        int value = node.get("side").asText().equals("Buy") ? node.get("size").intValue() : -node.get("size").intValue();
                        orderBook.insert(price, value);
                    }
                } else {
                    JsonNode data = response.get("data");

                    JsonNode delete = data.get("delete");
                    for (JsonNode node : delete) {
                        orderBook.delete(Double.parseDouble(node.get("price").asText()));
                    }

                    JsonNode update = data.get("update");
                    for (JsonNode node : update) {
                        double price = Double.parseDouble(node.get("price").asText());
                        int value = node.get("side").asText().equals("Buy") ? node.get("size").intValue() : -node.get("size").intValue();
                        orderBook.update(price, value);
                    }

                    JsonNode insert = data.get("insert");
                    for (JsonNode node : insert) {
                        double price = Double.parseDouble(node.get("price").asText());
                        int value = node.get("side").asText().equals("Buy") ? node.get("size").intValue() : -node.get("size").intValue();
                        orderBook.update(price, value);
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

    public MarketOrderBook orderBook() {
        return orderBook;
    }
}
