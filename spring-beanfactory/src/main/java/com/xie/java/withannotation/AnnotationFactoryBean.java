package com.xie.java.withannotation;

import com.xie.java.annotation.Tag;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AnnotationFactoryBean implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Class<?> type;

    private String name;

    private String topic;

    private Map<String,List<MethodInfo>> producerMethods;

    private Map<String,List<MethodInfo>> consumerMetods;

    public Object getObject() throws Exception {
        //创建代理
        //1.准备一些材料，把method 与topic ,tags 的关系存起来
        Method[] declaredMethods = type.getMethods();
        Map<Method,TopicTag> topicTagMap = new HashMap<Method,TopicTag>();
        for(Method method:declaredMethods){
            Tag annotation = method.getAnnotation(Tag.class);
            String tag = annotation.value();
            TopicTag tt = new TopicTag();
            tt.setTopic(topic);
            tt.setTag(tag);
            topicTagMap.put(method,tt);
        }

        //2.调用代理接口时，通过方法签名去匹配上面的信息
        Object proxy = Proxy.newProxyInstance(AnnotationFactoryBean.class.getClassLoader(), new Class[]{type}, new VirtualInvocationHandler(applicationContext,consumerMetods,topicTagMap));
        return proxy;
    }

    public Map<String, List<MethodInfo>> getProducerMethods() {
        return producerMethods;
    }

    public void setProducerMethods(Map<String, List<MethodInfo>> producerMethods) {
        this.producerMethods = producerMethods;
    }

    public Map<String, List<MethodInfo>> getConsumerMetods() {
        return consumerMetods;
    }

    public void setConsumerMetods(Map<String, List<MethodInfo>> consumerMetods) {
        this.consumerMetods = consumerMetods;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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
        this.applicationContext = applicationContext;
    }

    @Override
    public String toString() {
        return new StringBuilder("MyTestFactoryBean{")
                .append("type=").append(type).append(", ")
                .append("name='").append(name).append("', ")
                .append("topic='").append(topic).append("', ")
                .append("}").toString();
    }
}
