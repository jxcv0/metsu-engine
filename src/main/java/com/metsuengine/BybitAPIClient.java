package com.metsuengine;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metsuengine.Enums.OrderType;
import com.metsuengine.Enums.Side;
import com.metsuengine.Enums.TimeInForce;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BybitAPIClient {

    private static final Logger LOGGER = Logger.getLogger(BybitAPIClient.class.getName());
    private final OkHttpClient client;
    private final String symbol;

    public BybitAPIClient(String symbol) {
        this.symbol = symbol;
        this.client = new OkHttpClient();
    }

    public void placeOrder(String symbol, Side side, OrderType orderType, double price, int qty, TimeInForce timeInForce)  throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        TreeMap<String, String> requestParams = new TreeMap<>((o1, o2) -> o1.compareTo(o2));

        requestParams.put("side", side.toString());
        requestParams.put("symbol", symbol);
        requestParams.put("order_type", orderType.toString());
        requestParams.put("qty", qty + "");
        requestParams.put("price", price + "");
        requestParams.put("time_in_force", timeInForce.toString());
        requestParams.put("timestamp", ZonedDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli() + "");
        requestParams.put("api_key", APIKeys.key);

        String queryString = generateQueryString(requestParams);

        Request request = new Request.Builder()
            .post(RequestBody.create(new byte[0], null))
            .url("https://api.bybit.com/v2/private/order/create?" + queryString)
            .build();

        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            if (!response.isSuccessful()) {
                LOGGER.warning("Response unsuccessful");
                System.out.println(response.body().string());
            }
            getMessage(response);
            response.body().close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exeption caught while placing order", e);
        }
    }

    public void replaceOrder(String orderId, Order order)  throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        TreeMap<String, String> requestParams = new TreeMap<>((o1, o2) -> o1.compareTo(o2));

        int qty = (int) order.qty();
        requestParams.put("symbol", symbol);
        requestParams.put("p_r_qty", qty + "");
        requestParams.put("p_r_price", order.price() + "");
        requestParams.put("order_id", orderId);
        requestParams.put("timestamp", ZonedDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli() + "");
        requestParams.put("api_key", APIKeys.key);

        String queryString = generateQueryString(requestParams);
                
        Request request = new Request.Builder()
            .post(RequestBody.create(new byte[0], null))
            .url("https://api.bybit.com/v2/private/order/replace?" + queryString)
            .build();

        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            if (!response.isSuccessful()) {
                LOGGER.warning("Response unsuccessful");
                System.out.println(response.body().string());
            }
            getMessage(response);
            response.body().close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exeption caught while placing order", e);
        }
    }

    public void cancelOrder(String orderId)  throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        TreeMap<String, String> requestParams = new TreeMap<>((o1, o2) -> o1.compareTo(o2));

        requestParams.put("symbol", symbol);
        requestParams.put("order_id", orderId);
        requestParams.put("timestamp", ZonedDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli() + "");
        requestParams.put("api_key", APIKeys.key);

        String queryString = generateQueryString(requestParams);
                
        Request request = new Request.Builder()
            .post(RequestBody.create(new byte[0], null))
            .url("https://api.bybit.com/v2/private/order/cancel?" + queryString)
            .build();

        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            if (!response.isSuccessful()) {
                LOGGER.warning("Response unsuccessful");
                System.out.println(response.body().string());
            }
            getMessage(response);
            response.body().close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exeption caught while cencelling order", e);
        }
    }

    public void cancellAllOrders() {
        try {
            TreeMap<String, String> requestParams = new TreeMap<>((o1, o2) -> o1.compareTo(o2));

            requestParams.put("symbol", symbol);
            requestParams.put("timestamp", ZonedDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli() + "");
            requestParams.put("api_key", APIKeys.key);

            String queryString = generateQueryString(requestParams);
                    
            Request request = new Request.Builder()
                .post(RequestBody.create(new byte[0], null))
                .url("https://api.bybit.com/v2/private/order/cancelAll?" + queryString)
                .build();

            Call call = client.newCall(request);

            try {
                Response response = call.execute();
                if (!response.isSuccessful()) {
                    LOGGER.warning("Response unsuccessful");
                    System.out.println(response.body().string());
                }
                getMessage(response);
                response.body().close();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Exeption caught while cancelling orders", e);
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            // questionable
            e.printStackTrace(System.err);
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
    public static String generateQueryString(TreeMap<String, String> requestParams) throws NoSuchAlgorithmException, InvalidKeyException {
        Set<String> keySet = requestParams.keySet();
        Iterator<String> iterator = keySet.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            String key = iterator.next();
            stringBuilder.append(key).append("=").append(requestParams.get(key));
            stringBuilder.append("&");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(APIKeys.secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKey);

        return stringBuilder + "&sign=" + bytesToHex(sha256_HMAC.doFinal(stringBuilder.toString().getBytes()));
    }

    /**
     * Generates a query string for websocket private endpoints
     * 
     * @param params the alphabetically sorted TreeMap containing request parameters
     * @return the query string
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String generateWSQueryString(TreeMap<String, String> requestParams) throws NoSuchAlgorithmException, InvalidKeyException {
        Set<String> keySet = requestParams.keySet();
        Iterator<String> iterator = keySet.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            String key = iterator.next();
            stringBuilder.append(key).append("=").append(requestParams.get(key));
            stringBuilder.append("&");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(APIKeys.secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKey);

        return stringBuilder + "&signature=" + bytesToHex(sha256_HMAC.doFinal(stringBuilder.toString().getBytes()));
    }
    
    /**
     * Converts hashed string to hex
     * @param hash the hashed String
     * @return the converted hash
     */
    public static String bytesToHex(byte[] hash) {
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

    private void getMessage(Response response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.body().string());
            System.out.println(node.get("ret_code") + " " + node.get("ret_msg"));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, LOGGER.getName(), e);
        }
    }

    /**
     * @param from  The start date
     * @param to    The end date
     * @return      The trades that took place bwtween the "from" and "to" dates
     */
    public TradeSeries getTradingRecords(ZonedDateTime from, ZonedDateTime to) {

        String url = "https://api.bybit.com/v2/public/trading-records?symbol="+ this.symbol
            + "&from=" + from.toEpochSecond()
            + "&limit=1000";

        TradeSeries tradeSeries = new TradeSeries();

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
                    Trade trade = new Trade(
                        ZonedDateTime.parse(result.findValue("time").asText()),
                        result.findValue("side").asText(),
                        result.findValue("price").asDouble(),
                        result.findValue("qty").asDouble());
                    
                    if (trade.time().isAfter(from) && trade.time().isBefore(to)) {
                        tradeSeries.addtrade(trade);
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, LOGGER.getName(), e);
        }

        return tradeSeries;
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
            e.printStackTrace(System.err);
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
            e.printStackTrace(System.err);
        }
    }

    private ZonedDateTime secondsToZonedDateTime(long epochSecond) {
        Instant instant = Instant.ofEpochSecond(epochSecond);
        return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
