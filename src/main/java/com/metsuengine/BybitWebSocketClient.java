package com.metsuengine;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.json.JSONObject;

public class BybitWebSocketClient extends Thread {

    static String api_key = "";
    static String api_secret = "";
    static Session session;
    private String uri = "wss://stream.bytick.com/realtime";
    private String topic = null;
    private BybitWebSocket bybitWebSocket =  new BybitWebSocket();

    public BybitWebSocketClient(BybitWebSocket bybitWebSocket, String topic) {
        this.bybitWebSocket = bybitWebSocket;
        this.topic = topic;        
    }

    public static String generate_signature(String expires){
        return sha256_HMAC("GET/realtime"+ expires, api_secret);
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    public static String sha256_HMAC(String message, String secret) {
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

    public static String getAuthMessage(){
        JSONObject req = new JSONObject();
        req.put("op", "auth");
        List<String> args = new LinkedList<String>();
        String expires = String.valueOf(System.currentTimeMillis()+1000);
        args.add(api_key);
        args.add(expires);
        args.add(generate_signature(expires));
        req.put("args", args);
        return (req.toString());
    }

    public static String subscribe(String op, String argv){
        JSONObject req = new JSONObject();
        req.put("op", op);
        List<String> args = new LinkedList<String>();
        args.add(argv);
        req.put("args", args);
        return req.toString();
    }

    @Override
    public void run() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            String uri = this.uri;
            container.connectToServer(this.bybitWebSocket, URI.create(uri));
            // session.getBasicRemote().sendText(getAuthMessage());
            session.getBasicRemote().sendText(subscribe("subscribe", this.topic));

            while(true) {
                session.getBasicRemote().sendText("ping");
                Thread.sleep(30000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
