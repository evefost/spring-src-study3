package com.xie.java.service;

import com.xie.java.annotation.Tag;
import com.xie.java.annotation.Topic;

/**
 * Created by xieyang on 18/7/14.
 */
@Topic(value = "TopicB")
public interface ServiceC {

    @Tag(value = "testB")
    void testA(String a);
}
