package com.metsuengine;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.json.JSONObject;

public class BybitWebSocketClient extends Thread {

    static String api_key = ""; // TODO
    static String api_secret = ""; // TODO
    static Session session;
    private final String topic;
    private final BybitTradeWebSocket tradeWebSocket;

    public BybitWebSocketClient(TickSeries tickSeries, String topic) {
        this.tradeWebSocket = new BybitTradeWebSocket(tickSeries);
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
            container.connectToServer(tradeWebSocket, URI.create("wss://stream.bytick.com/realtime"));
            session.getBasicRemote().sendText(subscribe("subscribe", topic));

            while(true) {
                session.getBasicRemote().sendText("ping");
                Thread.sleep(30000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
