package com.xie.java.datasource;

import com.xie.java.datasource.annotation.DatabaseId;
import com.xie.java.datasource.interceptor.ServiceInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/7/4
 */
@Component
public class ServiceDatabaseProcessor implements BeanPostProcessor, BeanFactoryAware {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isMarkDatabaseId(beanName)) {
            Class<?>[] interfaces = bean.getClass().getInterfaces();
            return Proxy.newProxyInstance(this.getClass().getClassLoader(), interfaces, new ServiceInterceptor(bean));
        }
        return bean;
    }


    private boolean isMarkDatabaseId(String beanName) {

        BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
        try {
            Class<?> clz = ServiceDatabaseProcessor.class.getClassLoader().loadClass(beanDefinition.getBeanClassName());
            if (clz.isAnnotationPresent(DatabaseId.class)) {
                return true;
            }
            Class<?>[] interfaces = clz.getInterfaces();
            for (Class<?> ifc : interfaces) {
                if (ifc.isAnnotationPresent(DatabaseId.class)) {
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private BeanDefinitionRegistry registry;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        registry= (BeanDefinitionRegistry) beanFactory;
    }
}
