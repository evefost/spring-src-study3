package com.xie.java.xsd.handler;

import com.xie.java.xsd.beans.Teacher;
import com.xie.java.xsd.parser.StudentBeanDefinitionParser;
import com.xie.java.xsd.parser.TeacherBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 2.自定义命名schema 处理器,
 */
public class PersonNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        //如果有多种不种标签，都可以在这里注册（自定义标签 user,other）
        registerBeanDefinitionParser("student", new StudentBeanDefinitionParser());
        registerBeanDefinitionParser("teacher", new TeacherBeanDefinitionParser(Teacher.class));

    }

}