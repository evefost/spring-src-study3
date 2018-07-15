package com.xie.java.simple;

import com.xie.java.InterFaceA;
import com.xie.java.simple.BeanRegistrar;
import com.xie.java.simple.MyTestFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@EnableFeignClients
public class Main {

    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        BeanDefinitionRegistry  registry = context;
        //BeanRegistrar 先注册到容器内
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(BeanRegistrar.class);

       registry.registerBeanDefinition("myBeanRegistrar",genericBeanDefinition);
       // context.register(ProxyInterface.class);
        context.scan("com.xie.java");
        context.refresh();
        BeanRegistrar bean = context.getBean(BeanRegistrar.class);
        Set<String> packages = new HashSet<String>();
        packages.add("com.xie.java");
        bean.registerVirtualApis(packages,registry);

        InterFaceA faceA = (InterFaceA) context.getBean(InterFaceA.class);
//        Field[] declaredFields = faceA.getClass().getDeclaredFields();
//        for(Field field:declaredFields){
//            field.setAccessible(true);
//            System.out.println("====="+field.get(faceA));
//        }

        faceA.testA("66666666");
        faceA.toString();
        MyTestFactoryBean factoryBeana = (MyTestFactoryBean) context.getBean("&Proxy$com.xie.java.InterFaceA");
        MyTestFactoryBean factoryBeanb = (MyTestFactoryBean) context.getBean("&Proxy$com.xie.java.InterfaceB");
        System.out.println(context);
    }


}
