package com.xie.java.service;

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

    @Tag(value = "addUser")
    String addUser(User user){
        System.out.println("收到添加用户");
       return "收到添加用户";
    };


}
