package com.xie.java.xsd.parser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * Created by xieyang on 17/7/10.
 */
public class TeacherBeanDefinitionParser implements BeanDefinitionParser {

    private Class<?> beanClass;

    public TeacherBeanDefinitionParser(Class<?> beanClass){
        this.beanClass = beanClass;
    }
    
    
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        doParse(element,beanDefinition);
        //未处理nest 依懒
        //bean 注删必须有id
        String id = resolveId(element, beanDefinition, parserContext);
        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition,id);
        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, parserContext.getRegistry());
        if(this.shouldFireEvents()){
            //这是干什么的?通知事件?
            BeanComponentDefinition componentDefinition = new BeanComponentDefinition(beanDefinitionHolder);
            parserContext.registerComponent(componentDefinition);
        }


        return beanDefinition;
    }

    private void doParse(Element element,RootBeanDefinition beanDefinition){
        String className = element.getAttribute("className");
        beanDefinition.getPropertyValues().addPropertyValue("className",className);

    }

    protected String resolveId(Element element,RootBeanDefinition beanDefinition ,ParserContext parserContext){
        String id = element.getAttribute("id");
        if(!StringUtils.hasText(id)) {
            String simpleName = beanDefinition.getBeanClass().getSimpleName();
            id = simpleName.substring(0,1).toLowerCase()+simpleName.substring(1,simpleName.length());
        }
        System.out.println("bean id:"+id);
        return id;
    }

    protected boolean shouldFireEvents() {
        return true;
    }

}
