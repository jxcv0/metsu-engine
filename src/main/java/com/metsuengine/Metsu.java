package com.metsuengine;

import com.metsuengine.WebSockets.BybitInversePerpetualOrderBookWebsocket;
import com.metsuengine.WebSockets.BybitInversePerpetualSubscriptionSet;
import com.metsuengine.WebSockets.BybitInversePerpetualTradeWebSocket;
import com.metsuengine.WebSockets.BybitWebSocketClient;

import org.json.JSONArray;
import org.json.JSONObject;

public class Metsu {
    public static void main( String[] args ) {

        test("BTC-USDT", "level2");

        final MarketOrderBook orderBook = new MarketOrderBook();
        final TickSeries tickSeries = new TickSeries(10);

        Controller controller = new Controller(tickSeries, orderBook);

        BybitWebSocketClient client = new BybitWebSocketClient(
            new BybitInversePerpetualSubscriptionSet(new BybitInversePerpetualOrderBookWebsocket(orderBook),
                "wss://stream.bytick.com/realtime",
                "orderBook_200.100ms.BTCUSD"),
            new BybitInversePerpetualSubscriptionSet(new BybitInversePerpetualTradeWebSocket(tickSeries),
                "wss://stream.bytick.com/realtime",
                "trade.BTCUSD")
        );

        client.run();
    }

    public static void test(String productId, String channel) {

        JSONObject request = new JSONObject();
        request.put("type", "subscribe");

        JSONArray idArray = new JSONArray();
        idArray.put(productId);

        request.put("product_ids", productId);
    }
}