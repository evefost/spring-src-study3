package bean.test;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by Administrator on 2018/2/10.
 */
 //@Component
public class Test1 {


  public static void main(String[] args){
    ClassPathResource classPathResource = new ClassPathResource("spring-beans.xml");
    BeanFactory factory = new XmlBeanFactory(classPathResource);
    Object testBean = factory.getBean("testBean");
    System.out.println(testBean);
  }

}
