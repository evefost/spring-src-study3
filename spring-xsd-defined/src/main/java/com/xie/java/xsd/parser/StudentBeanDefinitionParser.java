package com.xie.java.xsd.parser;

import com.xie.java.xsd.beans.Student;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * 3.自定义专用用于解释User对应标签的beandefinitionparser，
 * 如里有其它不同的标签，可再定义对应的parser
 */
public class StudentBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    protected Class getBeanClass(Element element) {
        return Student.class;
    }

    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        //解释定义userName元素
        String userName = element.getAttribute("userName");
        String email = element.getAttribute("email");
        if (StringUtils.hasText(userName)) {
            builder.addPropertyValue("userName", userName);
        }
        if (StringUtils.hasText(email)) {
            builder.addPropertyValue("email", email);
        }
    }
}

