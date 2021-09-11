package com.metsuengine;

public class Metsu {
    public static void main( String[] args ){

        Thread websocketThread = new Thread( new BybitWebSocketClient("trade.BTCUSD"));
        websocketThread.start();

        System.out.println("here");

    }
}