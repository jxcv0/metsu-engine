package com.metsuengine;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.json.JSONObject;

public class BybitWebSocketClient extends Thread {
    
    private final String symbol;
    static Session session;
    final ClientEndpointConfig cec;
    private final BybitAnnotatedEndpoint endpoint;

    public BybitWebSocketClient(String symbol, TradeSeries tradeSeries, LimitOrderBook orderBook, QuotePair quotes, Position position) {
        this.symbol = symbol;
        this.cec = ClientEndpointConfig.Builder.create().build();
        this.endpoint = new BybitAnnotatedEndpoint(symbol, tradeSeries, orderBook, quotes, position);
    }

    private String generate_signature(String expires){
        return sha256_HMAC("GET/realtime"+ expires, APIKeys.secret);
    }

    private String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    private String sha256_HMAC(String message, String secret) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hash;

    }

    private String getAuthMessage(){
        JSONObject req=new JSONObject();
        req.put("op", "auth");
        List<String> args = new LinkedList<String>();
        String expires = String.valueOf(System.currentTimeMillis() + 2500);
        args.add(APIKeys.key);
        args.add(expires);
        args.add(generate_signature(expires));
        req.put("args", args);
        return (req.toString());
    }

    private String subscribe(String op, List<String> argv){
        JSONObject request = new JSONObject();
        request.put("op", op);
        List<String> args = new LinkedList<>();
        argv.forEach(a -> args.add(a));
        request.put("args", args);
        System.out.println(request.toString());
        return request.toString();
    }

    @Override
    public void run() {
        try {
            List<String> topics = new ArrayList<>();
            topics.add("trade." + symbol);
            topics.add("orderBook_200.100ms." + symbol);
            topics.add("position");
            topics.add("order");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(endpoint, URI.create("wss://stream.bytick.com/realtime"));
            session.getBasicRemote().sendText(getAuthMessage());
            session.getBasicRemote().sendText(subscribe("subscribe", topics));

            while(true) {
                session.getBasicRemote().sendText("{\"op\":\"ping\"}");
                sleep(30000);
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
