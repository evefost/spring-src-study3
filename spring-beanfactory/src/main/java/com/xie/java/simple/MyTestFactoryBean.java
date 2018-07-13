package com.xie.java.simple;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;

class MyTestFactoryBean implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {

    private Class<?> type;

    private String name;

    private String url;

    private String path;



    public Object getObject() throws Exception {
        //创建代理
        Object proxy = Proxy.newProxyInstance(MyTestFactoryBean.class.getClassLoader(), new Class[]{type}, new MyInvocaHandler());
        return proxy;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public Class<?> getObjectType() {
        return this.type;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {

    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public String toString() {
        return new StringBuilder("MyTestFactoryBean{")
                .append("type=").append(type).append(", ")
                .append("name='").append(name).append("', ")
                .append("url='").append(url).append("', ")
                .append("path='").append(path).append("', ")
                .append("}").toString();
    }
}
