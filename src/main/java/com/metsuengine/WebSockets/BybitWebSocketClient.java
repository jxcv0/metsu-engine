package com.metsuengine.WebSockets;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.metsuengine.APIKeys;

import org.json.JSONObject;

public class BybitWebSocketClient extends Thread {

    static Session session;
    private final List<BybitInversePerpetualSubscriptionSet> subscriptionSets;

    @SafeVarargs
    public BybitWebSocketClient(BybitInversePerpetualSubscriptionSet... sets) {
        this.subscriptionSets = new ArrayList<>(
            Arrays.asList(sets)
        );    
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

    private String subscribe(String op, String argv){
        JSONObject request = new JSONObject();
        request.put("op", op);
        List<String> args = new LinkedList<>();
        args.add(argv);
        request.put("args", args);
        return request.toString();
    }

    @Override
    public void run() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            for (BybitInversePerpetualSubscriptionSet set : subscriptionSets) {
                container.connectToServer(set.getHandler(), URI.create(set.getURI()));
                session.getBasicRemote().sendText(getAuthMessage());
                session.getBasicRemote().sendText(subscribe("subscribe", set.getTopic()));
            }

            while(true) {
                session.getBasicRemote().sendText("{\"op\":\"ping\"}");
                sleep(30000);
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
