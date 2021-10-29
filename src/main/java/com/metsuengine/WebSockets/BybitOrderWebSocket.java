package com.metsuengine.WebSockets;

import java.io.IOException;
import java.util.List;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metsuengine.Order;
import com.metsuengine.Tick;
import com.metsuengine.TickSeries;
import com.metsuengine.WebSocketHandler;

@ClientEndpoint
public class BybitOrderWebSocket implements WebSocketHandler {

    private final List<Order> orders;

    public BybitOrderWebSocket(List<Order> orders) {
        this.orders = orders;
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

                // TODO - parse JSON to Orders
                // replace if different
                if (!data.isNull()) {
                    for (JsonNode node : data) {
                        Order order = new Order(symbol, side, orderType, price, qty, timeInForce, orderStatus)                        
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

    public TickSeries tickSeries() {
        return tickSeries;
    }
}