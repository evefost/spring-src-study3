package com.xie.java.beans;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xieyang on 19/4/17.
 */
public class Tesss implements FactoryBean {

    @Autowired
    public Object getObject() throws Exception {
        return new Object();
    }

    public Class<?> getObjectType() {
        return null;
    }

    public boolean isSingleton() {
        return false;
    }
}
