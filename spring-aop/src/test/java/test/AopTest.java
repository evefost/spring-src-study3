package test;

import com.xie.java.aop.TestBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

import java.util.Map;

/**
 * Created by Administrator on 2018/2/10.
 */
 //@Component
public class AopTest {


  public static void main(String[] args) throws BeansException {
    ClassPathResource resource = new ClassPathResource("spring-aop.xml");
    DefaultListableBeanFactory factory = new  DefaultListableBeanFactory();
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
    reader.loadBeanDefinitions(resource);

    Map<String, BeanPostProcessor> beansOfType = factory.getBeansOfType(BeanPostProcessor.class);
    beansOfType.forEach((name,bean)->{
      factory.addBeanPostProcessor(bean);
    });
    factory.preInstantiateSingletons();
    TestBean testBean = (TestBean) factory.getBean("testBean");
    System.out.println("==================");
    testBean.test();
    System.out.println("==================");

  }

}
