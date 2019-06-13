package com.xie.java.beans.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/6/13
 */
public class Processor1 implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        if (bean instanceof ITestProcessor) {
            Class<?> aClass = bean.getClass();
            Class<?>[] interfaces = aClass.getInterfaces();
            Object proxy = Proxy.newProxyInstance(getClass().getClassLoader(), interfaces, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    try {
                        System.out.println("processor1 处理前.....");
                        return method.invoke(bean, objects);
                    } finally {
                        System.out.println("processor1 处理后.....");
                    }

                }
            });
            return proxy;
        }
        return bean;
    }
}
