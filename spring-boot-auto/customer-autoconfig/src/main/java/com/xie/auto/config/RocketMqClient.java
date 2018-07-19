package com.xie.auto.config;

public class RocketMqClient implements Client{

    public boolean start() {
        System.out.println("rocket client start");
        return true;
    }

    public boolean shutdown() {
        System.out.println("rocket client shutdown");
        return true;
    }
}
