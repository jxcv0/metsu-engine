package com.metsuengine;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BybitEndpoint {

    private static final Logger LOGGER = Logger.getLogger(BybitEndpoint.class.getName());

    private String symbol;

    public BybitEndpoint(String symbol) {
        this.symbol = symbol;
    }

    public TickSeries getTradingRecords(ZonedDateTime from, ZonedDateTime to) {

        String url = "https://api.bybit.com/v2/public/trading-records?symbol="+ this.symbol
            + "&from=" + from.toEpochSecond()
            + "&limit=1000";

        TickSeries tickSeries = new TickSeries();

        try {
            Request request = new Request.Builder()
                .url(url)
                .build();
            
            LOGGER.info(request.toString());
                        
            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            Response response = call.execute();
            String content = response.body().string();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(content);

            if (jsonNode.has("result")) {
                JsonNode results = jsonNode.findValue("result");

                for (JsonNode result : results) {
                    Tick tick = new Tick(
                        ZonedDateTime.parse(result.findValue("time").asText()),
                        result.findValue("side").asText(),
                        result.findValue("price").asDouble(),
                        result.findValue("qty").asDouble());
                    
                    if (tick.time().isAfter(from) && tick.time().isBefore(to)) {
                        tickSeries.add(tick);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tickSeries;
    }

    public void getKlineRecords(int from) {

        String url = "https://api.bybit.com/v2/public/kline/list?symbol=" + this.symbol + "&interval=1&limit=1&from=" + from;
        
        CSVManager manager = new CSVManager("src\\main\\resources\\BTCUSD-03-10-21-minus1month.csv");

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
                        secondsToZonedDateTime(result.findValue("open_time").asLong()).toString(),
                        result.findValue("open").asText(),
                        result.findValue("high").asText(),
                        result.findValue("low").asText(),
                        result.findValue("close").asText(),
                        result.findValue("volume").asText()
                    };

                    manager.writeLine(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ZonedDateTime secondsToZonedDateTime(long epochSecond) {
        Instant instant = Instant.ofEpochSecond(epochSecond);
        return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
