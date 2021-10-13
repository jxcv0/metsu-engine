package com.metsuengine;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.json.JSONObject;

public class BybitWebSocketClient implements Runnable {

    static Session session;
    private final List<SubscriptionSet> subscriptionSets;

    @SafeVarargs
    public BybitWebSocketClient(SubscriptionSet... sets) {
        this.subscriptionSets = new ArrayList<SubscriptionSet>();
        for (SubscriptionSet set : sets) {
            this.subscriptionSets.add(set);
        }      
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
            for (SubscriptionSet set : subscriptionSets) {
                container.connectToServer(set.getWebSocket(), URI.create(set.getURI()));
                session.getBasicRemote().sendText(subscribe("subscribe", set.getTopic()));
            }

            while(true) {
                session.getBasicRemote().sendText("{\"op\":\"ping\"}");
                Thread.sleep(30000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
