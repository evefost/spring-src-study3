package com.xie.java.xsd.test;


import com.xie.java.xsd.beans.Student;
import com.xie.java.xsd.beans.Teacher;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class SpringCustomTest {

    public static void main(String[] args) {

        Resource classPathResource = new ClassPathResource("test.xml");
        BeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
        BeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
        reader.loadBeanDefinitions(classPathResource);
        Student user = (Student) factory.getBean("testStudent");
        Teacher other = (Teacher) factory.getBean("teacher");
        Teacher bean = (Teacher) factory.getBean(Teacher.class);
        Object school = factory.getBean("school");
        System.out.println(user.getUserName());
    }

}
