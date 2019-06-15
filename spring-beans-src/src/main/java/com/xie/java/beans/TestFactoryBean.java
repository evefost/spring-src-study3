package com.xie.java.beans;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xieyang on 19/4/17.
 */
public class TestFactoryBean implements FactoryBean {

    @Autowired
    public Object getObject() throws Exception {
        return new User();
    }

    public Class<?> getObjectType() {
        return User.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
