package com.metsuengine;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class CoinbaseWebsocketClientTest {

    @Test
    public void subscribeTest() {
        String productId = "";
        String channels = "";

        JSONObject request = new JSONObject();
        request.put("type", "subscribe");

        JSONArray idArray = new JSONArray();
        idArray.put(productId);

        request.put("product_ids", productId);

    }
    
}
