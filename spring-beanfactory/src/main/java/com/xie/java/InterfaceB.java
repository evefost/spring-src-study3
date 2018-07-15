package com.xie.java;

import com.xie.java.annotation.Producer;
import com.xie.java.annotation.Tag;
import com.xie.java.annotation.Topic;


@Producer
@Topic("TopicB")
public interface InterfaceB {

    @Tag(value = "testA")
    void testA(String a);

    @Tag(value = "testB")
    String testB(String a);

}
