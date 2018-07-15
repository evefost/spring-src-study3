package com.xie.java.withannotation;

import com.xie.java.InterFaceA;
import com.xie.java.beans.User;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashSet;
import java.util.Set;

/**
 * 模拟一个接品，通过mq发送主题及top
 */
@EnableFeignClients
public class Main {

    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException {


        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-beans.xml");
        context.refresh();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) context.getBeanFactory();
        //BeanRegistrar 先注册到容器内
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(AnnotationBeanRegistrar.class);

        registry.registerBeanDefinition("myBeanRegistrar", genericBeanDefinition);


        AnnotationBeanRegistrar beanRegistrar = context.getBean(AnnotationBeanRegistrar.class);
        Set<String> packages = new HashSet<String>();
        packages.add("com.xie.java");
        packages.add("com.test.xie");
        beanRegistrar.registerVirtualApis(packages, registry);
        beanRegistrar.scanImplTopicTags(packages);

        InterFaceA faceA = context.getBean(InterFaceA.class);
        //faceA.testA("这是调用a 方法的参数");
        User user = new User();
        user.setName("xieyang");
        user.setAge(28);
        faceA.baseParams2("这是描述",19,user);


        //faceA.addUser(user);
//        InterfaceB cse = context.getBean(InterfaceB.class);
//        cse.testA("99999");


//        ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
//        Method[] methods = InterFaceA.class.getDeclaredMethods();
//        for(Method method:methods){
//            System.out.println("方法:"+method.getName());
//            String[] parameterNames = nameDiscoverer.getParameterNames(method);
//            if(parameterNames != null){
//                for(String name:parameterNames){
//                    System.out.println("name:"+name);
//                }
//            }
//
//        }

        System.out.println(context);
    }


}
