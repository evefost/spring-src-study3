package com.xie.java.withannotation;

import java.lang.reflect.Method;

/**
 * Created by xieyang on 18/7/14.
 */
public class MethodInfo {


    private Class targetClass;

    private Method method;

    private String topic;

    private String tag;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }
}
