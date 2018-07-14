package com.xie.java;

import com.xie.java.annotation.VirtualApi;
import com.xie.java.annotation.Tag;
import com.xie.java.annotation.Topic;
import com.xie.java.beans.User;

@VirtualApi
@Topic(value = "TopicA")
public interface InterFaceA {

    @Tag(value = "testA")
    void testA(String a);

    @Tag(value = "testB")
    String testB(String a);

    @Tag(value = "baseParams")
    String baseParams(String description,int age);

    @Tag(value = "baseParams2")
    String baseParams2(String description,int age,User user);

    @Tag(value = "addUser")
    String addUser(User user);


}
