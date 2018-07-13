package com.xie.java.withannotation;

import com.xie.java.InterFaceA;
import com.xie.java.beans.User;
import com.xie.java.simple.BeanRegistrar;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashSet;
import java.util.Set;

/**
 * 模拟一个接品，通过mq发送主题及top
 */
@EnableFeignClients
public class Main {

    public static void main(String[] args) throws IllegalAccessException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        BeanDefinitionRegistry registry = context;
        //BeanRegistrar 先注册到容器内
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(AnnotationBeanRegistrar.class);

        registry.registerBeanDefinition("myBeanRegistrar", genericBeanDefinition);
        context.refresh();
        BeanRegistrar bean = context.getBean(AnnotationBeanRegistrar.class);
        Set<String> packages = new HashSet<String>();
        packages.add("com.xie.java");
        bean.registerVirtualApis(packages, registry);

        InterFaceA faceA = context.getBean(InterFaceA.class);
        faceA.testA("这是调用a 方法的参数");
        User user = new User();
        user.setName("xieyang");
        user.setAge(28);
        faceA.addUser(user);
        AnnotationFactoryBean factoryBeana = (AnnotationFactoryBean) context.getBean("&Proxy$com.xie.java.InterFaceA");
        AnnotationFactoryBean factoryBeanb = (AnnotationFactoryBean) context.getBean("&Proxy$com.xie.java.InterfaceB");
        System.out.println(context);
    }


}
