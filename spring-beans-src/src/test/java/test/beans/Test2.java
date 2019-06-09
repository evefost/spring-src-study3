package test.beans;

import com.xie.java.beans.TestPostProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Created by Administrator on 2018/2/10.
 */
 //@Component
public class Test2 {


  public static void main(String[] args){

    Resource classPathResource = new ClassPathResource("spring-beans.xml");
    BeanFactory factory = new DefaultListableBeanFactory();

    BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
    BeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
    reader.loadBeanDefinitions(classPathResource);

    GenericBeanDefinition testPostBeanDefinition = new GenericBeanDefinition();
    testPostBeanDefinition.setBeanClass(TestPostProcessor.class);
    registry.registerBeanDefinition("testProcessor",testPostBeanDefinition);

    Object testProcessor = factory.getBean("testProcessor");
    ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) factory;
    configurableBeanFactory.addBeanPostProcessor((BeanPostProcessor) testProcessor);

    Object testBean = factory.getBean("testBean");
    System.out.println(testBean);
  }

}
