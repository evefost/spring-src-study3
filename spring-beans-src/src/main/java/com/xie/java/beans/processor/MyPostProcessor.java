package com.xie.java.beans.processor;

import com.xie.java.beans.annotatio.Component;
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
@Component
public class MyPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        if (bean instanceof ITarget) {
            Class<?> aClass = bean.getClass();
            Class<?>[] interfaces = aClass.getInterfaces();
            Object proxy = Proxy.newProxyInstance(getClass().getClassLoader(), interfaces, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    Throwable ex = null;
                    try {
                        System.out.println("开启事务...");
                        try {
                            return method.invoke(bean, objects);
                        }catch (Throwable throwable){
                            ex = throwable;
                            System.out.println("发生异常");
                            throw  throwable;
                        }

                    } finally {
                        if(ex == null){
                            System.out.println("提交事务....");
                        }else {
                            System.out.println("发生异常，rollback data "+ex.getLocalizedMessage());
                        }
                    }
                }
            });
            return proxy;
        }
        return bean;
    }
}
