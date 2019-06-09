package test.beans;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Created by xieyang on 19/6/9.
 */
public class BeanFactoryTest {


    /**
     * 加载默认标签定议的beanDefinitions
     */
    @Test
    public void defaultXmlBeanDefinitions(){
        Resource classPathResource = new ClassPathResource("spring-beans.xml");
        BeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
        BeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
        reader.loadBeanDefinitions(classPathResource);
        Object testBean = factory.getBean("testBean");
        System.out.println("testBean="+testBean);
        Object users = factory.getBean("users");
        System.out.println("users="+users);

    }

    @Test
    public void customerXmlBeanDefinitions(){
        Resource classPathResource = new ClassPathResource("spring-beans.xml");
        BeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
        BeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
        reader.loadBeanDefinitions(classPathResource);

    }
}
