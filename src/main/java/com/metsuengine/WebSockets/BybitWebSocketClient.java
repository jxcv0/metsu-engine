package com.metsuengine.WebSockets;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

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
