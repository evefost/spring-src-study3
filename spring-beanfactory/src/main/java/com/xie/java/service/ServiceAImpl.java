package com.xie.java.service;

import com.alibaba.fastjson.JSON;
import com.xie.java.annotation.Tag;
import com.xie.java.annotation.Topic;
import com.xie.java.beans.User;
import org.springframework.stereotype.Service;

/**
 * Created by xieyang on 18/7/14.
 */
@Service
@Topic(value = "TopicA")
public class ServiceAImpl {


    @Tag(value = "testA")
    void testA(String a){
        System.out.println("test 方法被执行:"+a);
    }

    @Tag(value = "testB")
    void testB(String a,int age){
        System.out.println("test 方法被执行:"+a+"age"+age);
    }

    @Tag(value = "baseParams2")
    String baseParams2(String description,int age,User user){
        System.out.println("serverAimpl 多参数 方法被执行: "+description+"--age "+age+"==user: "+ JSON.toJSONString(user));

        return null;
    }

    @Tag(value = "addUser")
    String addUser(User user){
        System.out.println("收到添加用户: "+JSON.toJSONString(user));
       return "收到添加用户";
    };


}
