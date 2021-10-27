package com.metsuengine;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BybitRestAPIClient {

    private static final Logger LOGGER = Logger.getLogger(BybitRestAPIClient.class.getName());
    private final OkHttpClient client;
    private final String symbol;

    public BybitRestAPIClient(String symbol) {
        this.symbol = symbol;
        this.client = new OkHttpClient();
    }

    public void getOrderList() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        
        TreeMap<String, String> requestParams = new TreeMap<String, String>(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
            
        });

        requestParams.put("symbol", symbol);
        requestParams.put("timestamp", ZonedDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli() + "");
        requestParams.put("api_key", APIKeys.key);

        String queryString = generateQueryString(requestParams);
        
        Request request = new Request.Builder()
            .url("https://api.bybit.com/v2/private/order?" + queryString)
            .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a query string for private endpoints
     * 
     * @param params the alphabetically sorted TreeMap containing request parameters
     * @return the query string
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public String generateQueryString(TreeMap<String, String> requestParams) throws NoSuchAlgorithmException, InvalidKeyException {
        Set<String> keySet = requestParams.keySet();
        Iterator<String> iterator = keySet.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            String key = iterator.next();
            stringBuilder.append(key + "=" + requestParams.get(key));
            stringBuilder.append("&");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(APIKeys.secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKey);

        return stringBuilder + "&sign=" + bytesToHex(sha256_HMAC.doFinal(stringBuilder.toString().getBytes()));
    }
    
    /**
     * Converts hashed string to hex
     * @param hash the hashed String
     * @return the converted hash
     */
    private String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * @param from  The start date
     * @param to    The end date
     * @return      The trades that took place bwtween the "from" and "to" dates
     */
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
                        tickSeries.addTick(tick);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tickSeries;
    }

    
    public void getKlineRecord(int from) {

        String url = "https://api.bybit.com/v2/public/kline/list?symbol=" + symbol + "&interval=1&limit=1&from=" + from;

        String dateTime = DateTimeFormatter.ofPattern("dd-MM").format(ZonedDateTime.now());
        CSVManager manager = new CSVManager("src\\main\\resources\\" + symbol + dateTime + ".csv");

        try {
            Request request = new Request.Builder()
                .url(url)
                .build();
                        
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

    public void get200KlineRecords(int from, String interval) {

        String url = "https://api.bybit.com/v2/public/kline/list?symbol=" + symbol + "&interval=" + interval + "&from=" + from;

        String dateTime = DateTimeFormatter.ofPattern("dd-MM").format(ZonedDateTime.now());
        CSVManager manager = new CSVManager("src\\main\\resources\\" + symbol + dateTime + ".csv");

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
