package com.metsuengine;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BybitEndpoint {

    public static Bar createBarSeries(String symbol, String interval, ZonedDateTime from) {

        String url =
            "https://api.bybit.com/v2/public/kline/list?symbol=" + symbol + 
            "&interval=" + interval + 
            "&from=" + (from.toInstant().toEpochMilli()/1000) +
            "&limit=1";

        Bar bar = null;

        try {
            Request request = new Request.Builder()
                .url(url)
                .build();
                        
            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            Response response = call.execute();
            String content = response.body().string();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(content);

            if (jsonNode.has("result")) {
                JsonNode result = jsonNode.findValue("result");

                for (JsonNode results : result) {
                    bar = new BaseBar(
                        Duration.ofMinutes(Integer.parseInt(interval)),
                        secondsToZonedDateTime(results.findValue("open_time").asLong()),
                        results.findValue("open").asDouble(),
                        results.findValue("high").asDouble(),
                        results.findValue("low").asDouble(),
                        results.findValue("close").asDouble(),
                        results.findValue("volume").asDouble());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bar;
    }
    
    public static OrderBook getOrderBook(String symbol) {

        OrderBook orderBook = new OrderBook(symbol);

        try {
            Request request = new Request.Builder()
            .url("https://api.bybit.com/v2/public/orderBook/L2?symbol=" + symbol)
            .build();

            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            Response response = call.execute();
            String content = response.body().string();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(content);

            if (jsonNode.has("result")) {
                JsonNode result = jsonNode.findValue("result");

                for (int i = 0; i < result.size(); i++) {
                    JsonNode level = result.get(i);
                    String side = level.findValue("side").asText();

                    if (side.equals("Buy")) {
                        orderBook.insertBidLevel(level.findValue("price").asDouble(), level.findValue("size").asInt());
                        
                    } else if (side.equals("Sell")) {
                        orderBook.insertAskLevel(level.findValue("price").asDouble(), level.findValue("size").asInt());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderBook;
    }

    public static List<Liquidation> getLiquidations(String symbol, ZonedDateTime startTime) {

        List<Liquidation> liquidations = new ArrayList<Liquidation>();

        Instant instant = startTime.toInstant();

        Request request = new Request.Builder()
            .url("https://api.bybit.com/v2/public/liq-records?symbol=" + symbol + "&start_time=" + instant.toEpochMilli())
            .build();
        
        try {
            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            Response response = call.execute();
            String content = response.body().string();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(content);

            if (jsonNode.has("result")) {
                JsonNode result = jsonNode.findValue("result");

                for (JsonNode results : result) {
                    liquidations.add(new Liquidation(
                        results.findValue("qty").asDouble(),
                        results.findValue("side").asText(),
                        epochtoZonedDateTime(results.findValue("time").asLong()),
                        results.findValue("symbol").asText(),
                        results.findValue("price").asDouble()));
                        
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return liquidations;
    }
    
    public static ZonedDateTime epochtoZonedDateTime(long milliseconds) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneOffset.UTC);
    }

    public static ZonedDateTime secondsToZonedDateTime(long seconds) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(seconds*1000), ZoneOffset.UTC);
    }
}
