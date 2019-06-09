package com.xie.java.xsd.parser;

import com.xie.java.xsd.beans.School;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

public class SchoolBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    //spring 解释得到类的
    protected Class getBeanClass(Element element) {
        return School.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String name = element.getAttribute("name");
        String  address= element.getAttribute("address");
        int  students= Integer.parseInt(element.getAttribute("students"));
        builder.addConstructorArgValue(name);
        builder.addConstructorArgValue(address);
        builder.addConstructorArgValue(students);


    }


}

