package com.metsuengine;

import java.io.IOException;
import java.util.HashMap;

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

    public static OrderBook orderBook = new OrderBook("BybitBTCUSD");

    public static HashMap<Double, Integer> orderBookBid = new HashMap<Double, Integer>();
    public static HashMap<Double, Integer> orderBookAsk = new HashMap<Double, Integer>();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
        try {
            BybitWebSocketClient.session=session;
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

                if (data.isArray()) {
                    // First response is array of objectNodes

                    for (int i = 0; i < data.size(); i++) {
                        JsonNode level = data.get(i);
                        String side = level.findValue("side").asText();

                        if (side.equals("Buy")) {
                            orderBook.insertBidLevel(level.findValue("price").asDouble(), level.findValue("size").asInt());
                            
                        } else if (side.equals("Sell")) {
                            orderBook.insertAskLevel(level.findValue("price").asDouble(), level.findValue("size").asInt());

                        }
                    }

                } else {
                    // Following responses are Delete, Update, or Insert

                    if (data.has("delete")) {
                        JsonNode delete = data.findValue("delete");

                        if (!delete.isEmpty()) {
                            for (int i = 0; i < delete.size()-1; i++) {
                                String side = delete.findValue("side").asText();
    
                                if (side.equals("Buy")) {
                                    orderBook.deleteBidLevel(delete.findValue("price").asDouble());
                                    
                                } else if (side.equals("Sell")) {
                                    orderBook.deleteAskLevel(delete.findValue("price").asDouble());

                                }
                            }
                        }
                    }
                    
                    if (data.has("update")) {
                        JsonNode update = data.findValue("update");

                        if (!update.isEmpty()) {
                            for (int i = 0; i < update.size()-1; i++) {
                                String side = update.findValue("side").asText();
                                
                                if (side.equals("Buy")) {
                                    orderBook.updateBidLevel(update.findValue("price").asDouble(), update.findValue("size").asInt());
    
                                } else if (side.equals("Sell")) {
                                    orderBook.updateAskLevel(update.findValue("price").asDouble(), update.findValue("size").asInt());

                                }
                            }
                        }
                    }
                    
                    if (data.has("insert")) {
                        JsonNode insert = data.findValue("insert");

                        if (!insert.isEmpty()) {
                            for (int i = 0; i < insert.size()-1; i++) {
                                String side = insert.findValue("side").asText();
    
                                if (side.equals("Buy")) {
                                    orderBook.insertBidLevel(insert.findValue("price").asDouble(), insert.findValue("size").asInt());

                                } else if (side.equals("Sell")) {
                                    orderBook.insertAskLevel(insert.findValue("price").asDouble(), insert.findValue("size").asInt());
                                }
                            }
                        }
                    }
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