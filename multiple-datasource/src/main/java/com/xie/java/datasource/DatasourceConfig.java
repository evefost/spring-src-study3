package com.xie.java.datasource;

import com.xie.java.datasource.annotation.DatabaseId;
import com.xie.java.datasource.interceptor.PreTransactionInterceptor;
import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xieyang on 19/7/4.
 */
@Component
public class DatasourceConfig implements BeanFactoryAware, ResourceLoaderAware, BeanClassLoaderAware, InitializingBean {

    private BeanDefinitionRegistry registry;

    protected ResourceLoader resourceLoader;

    protected ClassLoader classLoader;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        AdvisorAdapterRegistry instance = GlobalAdvisorAdapterRegistry.getInstance();

        if (beanFactory instanceof BeanDefinitionRegistry) {
            registry = (BeanDefinitionRegistry) beanFactory;
            String interceptorName = "org.springframework.transaction.interceptor.TransactionInterceptor#0";
            BeanDefinition interceptorBeanDf = registry.getBeanDefinition(interceptorName);
            interceptorBeanDf.setBeanClassName(PreTransactionInterceptor.class.getName());
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        findDatabaseId();
    }

    private void findDatabaseId() throws ClassNotFoundException {
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        Map<Method, MethodMapping> methodMappingMap = new HashMap<>();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
            Class<?> beanClass = classLoader.loadClass(beanDefinition.getBeanClassName());
            if (beanClass.isAnnotationPresent(DatabaseId.class)) {
                DatabaseId annotation = beanClass.getAnnotation(DatabaseId.class);
                parseMethodDatabaseId(annotation, beanClass, methodMappingMap);
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> ifc : interfaces) {
                    parseMethodDatabaseId(annotation, ifc, methodMappingMap);
                }
            } else {
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> ifc : interfaces) {
                    if (ifc.isAnnotationPresent(DatabaseId.class)) {
                        DatabaseId annotation = ifc.getAnnotation(DatabaseId.class);
                        parseMethodDatabaseId(annotation, ifc, methodMappingMap);
                    }

                }
            }
        }

        TransactionContextHolder.methodDatabaseMapping = methodMappingMap;
        ServiceContextHolder.methodDatabaseMapping = methodMappingMap;
    }


    private void parseMethodDatabaseId(DatabaseId annotation, Class<?> clz, Map<Method, MethodMapping> methodMappingMap) {
        String databaseId = annotation.value();
        Method[] methods = clz.getDeclaredMethods();
        for (Method m : methods) {
            MethodMapping methodMapping = new MethodMapping();
            methodMapping.setDatabaseId(databaseId);
            methodMapping.setMethod(m);
            methodMappingMap.put(m, methodMapping);
        }
    }

    public static class MethodMapping {

        private Method method;

        private String databaseId;

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public String getDatabaseId() {
            return databaseId;
        }

        public void setDatabaseId(String databaseId) {
            this.databaseId = databaseId;
        }
    }
}
