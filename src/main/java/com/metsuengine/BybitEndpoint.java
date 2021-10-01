package com.metsuengine;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BybitEndpoint {

    private String symbol;

    public BybitEndpoint(String symbol) {
        this.symbol = symbol;
    }

    public TradeSeries getTradingRecords(int limit) {

        String url = "https://api.bybit.com/v2/public/trading-records?symbol=" + this.symbol + "&limit=" + limit;

        TradeSeries tradeSeries = new TradeSeries();

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
                JsonNode results = jsonNode.findValue("result");

                for (JsonNode result : results) {
                    tradeSeries.addTrade(new Trade(
                        ZonedDateTime.parse(result.findValue("time").asText()),
                        result.findValue("side").asText(),
                        result.findValue("price").asDouble(),
                        result.findValue("qty").asDouble()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tradeSeries;
    }

    public void getKlineRecords(int from) {

        String url = "https://api.bybit.com/v2/public/kline/list?symbol=" + this.symbol + "&from=" + from;
        
        CSVManager manager = new CSVManager("src\\main\\resources\\BTCUSD2021-9-14.csv");

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
                JsonNode results = jsonNode.findValue("result");

                for (JsonNode result : results) {
                    String[] line = {
                        result.findValue("time").asText(),
                        result.findValue("side").asText(),
                        result.findValue("price").asText(),
                        result.findValue("qty").asText()
                    };

                    manager.writeLine(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
