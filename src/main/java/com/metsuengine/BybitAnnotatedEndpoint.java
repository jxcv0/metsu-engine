package com.metsuengine;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
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

@ClientEndpoint
public class BybitAnnotatedEndpoint {

    static final Logger LOGGER = Logger.getLogger(BybitAnnotatedEndpoint.class.getName());

    private final String symbol;
    private final TradeSeries tradeSeries;
    private final LimitOrderBook orderBook;
    private final OrderManager orders;
    private final Position position;

    public BybitAnnotatedEndpoint(String symbol, TradeSeries tradeSeries, LimitOrderBook orderBook, OrderManager orders, Position position) {
        this.symbol = symbol;
        this.tradeSeries = tradeSeries;
        this.orderBook = orderBook;
        this.orders = orders;
        this.position = position;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        LOGGER.info("Connected to endpoint: " + session.getRequestURI());
        try {
            BybitWebSocketClient.session = session;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode response = mapper.readTree(message);

            if(response.has("topic")) {
                if (response.get("topic").asText().equals("orderBook_200.100ms." + symbol)) {
                    handleLOBMessage(response);
                } else if (response.get("topic").asText().equals("trade." + symbol)) {
                    handleTradeMessage(response);
                } else if (response.get("topic").asText().equals("order")) {
                    handleOrderMessage(response);
                } else if (response.get("topic").asText().equals("position")) {
                    handlePositionMessage(response);
                } else {
                    System.out.println(response);
                }
            }
  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason){
        LOGGER.info("Connection closed: " + reason.getReasonPhrase());
    }

    @OnError
    public void onError(Session session, Throwable error){
        LOGGER.log(Level.SEVERE, "Error: ", error);
    }

    public void handleTradeMessage(JsonNode response) {
        if (response.has("data")) {
            JsonNode data = response.findValue("data");

            if (!data.isNull()) {
                Trade trade = new Trade(
                    data.findValue("timestamp").asText(),
                    data.findValue("side").asText(),
                    data.findValue("price").asDouble(),
                    data.findValue("size").asDouble());
                
                tradeSeries.addtrade(trade);
            }
        }
    }

    public void handleLOBMessage(JsonNode response) {
        if (response.has("type")) {
            JsonNode type = response.get("type");
            if (type.asText().equals("snapshot")) {
                JsonNode data = response.get("data");

                for (JsonNode node : data) {
                    double price = Double.parseDouble(node.get("price").asText());
                    double value = node.get("side").asText().equals("Buy") ? node.get("size").doubleValue() : -node.get("size").doubleValue();
                    orderBook.insertOrUpdate(price, value);
                }
            } else {
                orderBook.ready();
                JsonNode data = response.get("data");

                JsonNode delete = data.get("delete");
                for (JsonNode node : delete) {
                    orderBook.delete(Double.parseDouble(node.get("price").asText()));
                }

                JsonNode update = data.get("update");
                for (JsonNode node : update) {
                    double price = Double.parseDouble(node.get("price").asText());
                    double value = node.get("side").asText().equals("Buy") ? node.get("size").doubleValue() : -node.get("size").doubleValue();
                    orderBook.insertOrUpdate(price, value);
                }

                JsonNode insert = data.get("insert");
                for (JsonNode node : insert) {
                    double price = Double.parseDouble(node.get("price").asText());
                    double value = node.get("side").asText().equals("Buy") ? node.get("size").doubleValue() : -node.get("size").doubleValue();
                    orderBook.insertOrUpdate(price, value);
                }
            }
        }
    }
    
    public void handleOrderMessage(JsonNode response) {
        if (response.has("data")) {
            JsonNode data = response.findValue("data");
            if (!data.isEmpty()) {
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
                    
                    orders.proccessOrder(order);
                }
            }
        }
    }

    private void handlePositionMessage(JsonNode response) {
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
    }
}
