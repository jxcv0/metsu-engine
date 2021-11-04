package com.metsuengine;

import javax.websocket.MessageHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BybitMessageHandler implements MessageHandler.Whole<String> {

    private final String symbol;
    private final TradeSeries tradeSeries;
    private final LimitOrderBook orderBook;

    public BybitMessageHandler(String symbol, TradeSeries tradeSeries, LimitOrderBook orderBook) {
        this.symbol = symbol;
        this.tradeSeries = tradeSeries;
        this.orderBook = orderBook;
    }

    @Override
    public void onMessage(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode response = mapper.readTree(message);

            if(response.has("topic")) {
                if (response.get("topic").asText().equals("trade." + symbol)) {
                    handleTradeMessage(response);
                }
            }
  
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                
                tradeSeries.addTick(trade);
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
}
