package com.xie.java;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

/**
 * Created by xieyang on 19/6/22.
 */
public class MyApplicationContext extends ClassPathXmlApplicationContext {

    @Override
    protected ConfigurableEnvironment createEnvironment() {
        return new StandardEnvironment();
    }
}
