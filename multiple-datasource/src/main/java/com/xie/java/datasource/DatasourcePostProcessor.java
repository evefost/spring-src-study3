package com.xie.java.datasource;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.transaction.config.TransactionManagementConfigUtils;

/**
 * Created by xieyang on 19/7/4.
 */
@Component
public class DatasourcePostProcessor implements BeanFactoryAware{



    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if(beanFactory instanceof BeanDefinitionRegistry){
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
            String txAdvisorBeanName = TransactionManagementConfigUtils.TRANSACTION_ADVISOR_BEAN_NAME;
            BeanDefinition txAdvisorBeanDefinition = registry.getBeanDefinition(txAdvisorBeanName);
            MutablePropertyValues propertyValues = txAdvisorBeanDefinition.getPropertyValues();
            String interceptorName = "org.springframework.transaction.interceptor.TransactionInterceptor#0";
            BeanDefinition interceptorBeanDf = registry.getBeanDefinition(interceptorName);
            interceptorBeanDf.setBeanClassName("com.xie.java.datasource.MyTransactionInterceptor");
            System.out.printf("xx");
        }
    }
}
