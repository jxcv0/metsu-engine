package com.metsuengine;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.json.JSONObject;

public class BybitWebSocketClient extends Thread {

    static Session session;
    private final String uri;
    private final String topic;
    private final WebSocket websocket;

    public BybitWebSocketClient(WebSocket websocket, String uri, String topic) {
        this.websocket = websocket;
        this.uri = uri;
        this.topic = topic;        
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
            container.connectToServer(websocket, URI.create(uri));
            session.getBasicRemote().sendText(subscribe("subscribe", topic));

            while(true) {
                session.getBasicRemote().sendText("{\"op\":\"ping\"}");
                Thread.sleep(30000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
