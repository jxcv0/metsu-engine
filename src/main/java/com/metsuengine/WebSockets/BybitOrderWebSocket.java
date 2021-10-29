package com.metsuengine.WebSockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metsuengine.Enums.OrderStatus;
import com.metsuengine.Enums.OrderType;
import com.metsuengine.Enums.Side;
import com.metsuengine.Enums.TimeInForce;
import com.metsuengine.Order;
import com.metsuengine.QuotePair;
import com.metsuengine.WebSocketHandler;

@ClientEndpoint
public class BybitOrderWebSocket implements WebSocketHandler {

    private final QuotePair quotes;

    public BybitOrderWebSocket(QuotePair quotes) {
        this.quotes = quotes;
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
                if (!data.isNull()) {
                    List<Order> orders = new ArrayList<>();
                    for (JsonNode node : data) {
                        Order order = new Order(
                            node.get("symbol").asText(),
                            Side.valueOf(node.get("side").asText()),
                            OrderType.valueOf(node.get("order_type").asText()),
                            node.get("price").asDouble(),
                            node.get("qty").asDouble(),
                            TimeInForce.valueOf(node.get("time_in_force").asText()),
                            OrderStatus.valueOf(node.get("order_status").asText()));

                        order.setId(node.get("order_id").asText());

                        orders.add(order);
                    }
                    quotes.update(orders);
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