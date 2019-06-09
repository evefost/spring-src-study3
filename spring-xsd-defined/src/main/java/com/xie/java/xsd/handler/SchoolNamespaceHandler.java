package com.xie.java.xsd.handler;

import com.xie.java.xsd.parser.SchoolBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 2.自定义命名schema 处理器,
 */
public class SchoolNamespaceHandler extends NamespaceHandlerSupport {


    @Override
    public void init() {
        //如果有多种不种标签，都可以在这里注册
        registerBeanDefinitionParser("school", new SchoolBeanDefinitionParser());

    }

}