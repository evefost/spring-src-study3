package com.xie.java.service;

import org.springframework.stereotype.Service;

/**
 * Created by xieyang on 18/7/14.
 */
@Service
public class ServiceBImpl implements ServiceB {

    public void testA(String a) {
       System.out.println("serverB imple被调用了");
    }
}
