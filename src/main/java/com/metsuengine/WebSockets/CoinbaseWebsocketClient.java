package com.metsuengine.WebSockets;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.json.JSONArray;
import org.json.JSONObject;

public class CoinbaseWebsocketClient implements Runnable {

    static Session session;
    private final List<CoinbaseSubscriptionSet> subscriptionSets;

    @SafeVarargs
    public CoinbaseWebsocketClient(CoinbaseSubscriptionSet... sets) {
        this.subscriptionSets = new ArrayList<CoinbaseSubscriptionSet>(
            Arrays.asList(sets)
        );
    }
    
    private String subscribe(String productId, String channel) {
        String[] productIds = {productId};
        String[] channels = {channel};

        JSONObject request = new JSONObject();
        request.put("type", "subscribe");

        JSONArray idArray = new JSONArray();
        idArray.put(productIds);

        JSONArray channelArray = new JSONArray();
        channelArray.put(channels);

        request.put("product_ids", productIds);
        request.put("channels", channels);

        return request.toString();
    }

    @Override
    public void run() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            for (CoinbaseSubscriptionSet set : subscriptionSets) {
                container.connectToServer(set.getHandler(), URI.create("wss://ws-feed.exchange.coinbase.com"));
                session.getBasicRemote().sendText(subscribe(set.getProductId(), set.getChannel()));
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
