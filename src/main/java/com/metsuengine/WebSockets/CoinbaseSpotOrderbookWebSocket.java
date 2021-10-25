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
import com.metsuengine.MarketOrderBook;
import com.metsuengine.WebSocketHandler;

@ClientEndpoint
public class CoinbaseSpotOrderbookWebSocket implements WebSocketHandler{
    
    private final MarketOrderBook orderBook;

    public CoinbaseSpotOrderbookWebSocket(MarketOrderBook orderBook) {
        this.orderBook = orderBook;
    }

    @OnOpen
    public void onOpen(Session session) {
        LOGGER.info("Connected to Coinbase Spot endpoint: " + session.getRequestURI() + " " + session.getBasicRemote());
        try {
            CoinbaseWebsocketClient.session = session;
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
                    JsonNode bids = response.get("bids");
                    for (JsonNode node : bids) {
                        orderBook.insertOrUpdate(node.get(0).asDouble(), node.get(1).asDouble());
                    }

                    JsonNode asks = response.get("asks");
                    for (JsonNode node : asks) {
                        orderBook.insertOrUpdate(node.get(0).asDouble(), -node.get(1).asDouble());
                    }
                } else if (type.asText().equals("l2update")) {
                    if (response.has("changes")) {
                        JsonNode changes = response.get("changes");
                        for (JsonNode node : changes) {
                            if (node.get(0).asText().equals("buy")) {
                                orderBook.insertOrUpdate(node.get(1).asDouble(), node.get(2).asDouble());
                            } else if (node.get(0).asText().equals("sell")) {
                                orderBook.insertOrUpdate(node.get(1).asDouble(), -node.get(2).asDouble());
                            }
                        }                        
                    }
                }
            }

        } catch(Exception e) {
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
