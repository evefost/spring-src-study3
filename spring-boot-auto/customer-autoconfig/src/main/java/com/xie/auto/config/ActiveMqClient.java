package com.xie.auto.config;

public class ActiveMqClient implements Client {

    public boolean start() {
        System.out.println("activemq client start");
        return true;
    }

    public boolean shutdown() {
        System.out.println("active client shutdown");
        return true;
    }
}
